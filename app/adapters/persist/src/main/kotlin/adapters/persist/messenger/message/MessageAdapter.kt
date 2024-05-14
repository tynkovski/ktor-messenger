package adapters.persist.messenger.message

import adapters.persist.messenger.entities.ReaderToMessageSqlEntity
import adapters.persist.messenger.mappers.fromEntities
import adapters.persist.messenger.mappers.toReadersSqlEntities
import adapters.persist.messenger.mappers.toSqlEntity
import com.github.michaelbull.logging.InlineLogger
import core.models.MessageEntry
import core.models.MessageEntryNotFoundException
import core.outport.*

internal class MessageAdapter(
    private val messageRepository: MessageRepository,
    private val readerToMessageRepository: ReaderToMessageRepository
) : AddMessagePort,
    GetMessagePort,
    GetLastMessagePort,
    GetMessageCountPort,
    GetMessagesPagingPort,
    UpdateMessagePort,
    DeleteMessagePort,
    GetUnreadMessagesPort {

    private val logger = InlineLogger()

    @MustBeCalledInTransactionContext
    private fun upsertReaderEntries(
        messageId: Long,
        messageEntry: MessageEntry
    ): Collection<ReaderToMessageSqlEntity> {
        val readersToAdd = messageEntry.toReadersSqlEntities(messageId)
        return readerToMessageRepository.upsert(readersToAdd)
    }

    @MustBeCalledInTransactionContext
    private fun upsertMessageEntry(entry: MessageEntry): MessageEntry {
        val messageAdded = messageRepository.upsert(entry.toSqlEntity())
        val readersAdded = upsertReaderEntries(messageAdded.id!!, entry)
        return MessageEntry.fromEntities(messageAdded, readersAdded)
    }

    @MustBeCalledInTransactionContext
    override fun addMessage(entry: MessageEntry): MessageEntry {
        require(entry.id == null) { "message.id must be null" }
        return upsertMessageEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override fun getMessage(messageId: Long): MessageEntry {
        val messageEntity = messageRepository.getByIdOrNull(messageId)
            ?: throw MessageEntryNotFoundException(searchCriteria = "id=$messageId")

        val readerEntities = readerToMessageRepository.getReadersByMessageId(messageId)

        return MessageEntry.fromEntities(messageEntity, readerEntities)
    }

    @MustBeCalledInTransactionContext
    override fun getLastMessage(roomId: Long): MessageEntry? {
        val messageEntity = messageRepository.getLastMessageOfRoom(roomId)
            ?: return null

        val readerEntities = readerToMessageRepository.getReadersByMessageId(messageEntity.id!!)
        return MessageEntry.fromEntities(messageEntity, readerEntities)
    }

    @MustBeCalledInTransactionContext
    override fun getMessagesPaging(roomId: Long, page: Long, pageSize: Int): Collection<MessageEntry> {
        val messageEntities = messageRepository.getMessagesPaging(roomId,page,pageSize)
        return messageEntities.map { entity ->
            val messageId = entity.id!!
            val readerEntities = readerToMessageRepository.getReadersByMessageId(messageId)
            MessageEntry.fromEntities(entity, readerEntities)
        }
    }

    @MustBeCalledInTransactionContext
    override fun getMessageCount(roomId: Long): Long {
        return messageRepository.getMessagesCount(roomId)
    }

    @MustBeCalledInTransactionContext
    override fun updateMessage(entry: MessageEntry): MessageEntry {
        val messageId = requireNotNull(entry.id) { "message.id must not be null" }
        if (!messageRepository.hasEntityWithId(messageId)) {
            throw MessageEntryNotFoundException(searchCriteria = "id=$messageId")
        }
        return upsertMessageEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override fun deleteMessage(entry: MessageEntry) : MessageEntry {
        val messageId = requireNotNull(entry.id) { "message.id must not be null" }
        if (!messageRepository.hasEntityWithId(messageId)) {
            throw MessageEntryNotFoundException(searchCriteria = "id=$messageId")
        }

        return upsertMessageEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override fun getUnreadMessages(applicantId: Long, roomId: Long): Collection<MessageEntry> {
        val messageEntities = messageRepository.getUnreadMessages(applicantId, roomId)
        return messageEntities.map { entity ->
            val readerEntities = readerToMessageRepository.getReadersByMessageId(entity.id!!)
            MessageEntry.fromEntities(entity, readerEntities)
        }
    }
}