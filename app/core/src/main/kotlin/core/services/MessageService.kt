package core.services

import core.models.MessageEntry
import core.outport.*
import core.usecase.*
import java.time.LocalDateTime

private fun createMessage(
    senderId: Long,
    roomId: Long,
    text: String
) = MessageEntry(
    id = null,
    senderId = senderId,
    roomId = roomId,
    text = text,
    readBy = setOf(senderId),
    editedAt = null,
    sentAt = LocalDateTime.now()
)

private fun editMessage(
    message: MessageEntry,
    text: String
) = message.copy(
    text = text,
    editedAt = LocalDateTime.now(),
)

private fun readMessage(
    message: MessageEntry,
    readerId: Long
) = message.copy(
    readBy = message.readBy + readerId
)

internal class AddMessageService(
    private val addMessagePort: AddMessagePort,
    private val txPort: PersistTransactionPort,
) : AddMessageUsecase {
    override suspend fun createMessage(
        applicantId: Long,
        roomId: Long,
        text: String
    ): MessageEntry = txPort.withNewTransaction {
        val message = createMessage(senderId = applicantId, roomId = roomId, text = text)
        addMessagePort.addMessage(message)
    }
}

internal class GetMessageService(
    private val getMessagePort: GetMessagePort,
    private val txPort: PersistTransactionPort,
) : GetMessageUsecase {
    override suspend fun getMessage(messageId: Long): MessageEntry =
        txPort.withNewTransaction {
            getMessagePort.getMessage(messageId)
        }
}

internal class GetMessageCountService(
    private val getMessageCountPort: GetMessageCountPort,
    private val txPort: PersistTransactionPort,
) : GetMessageCountUsecase {
    override suspend fun getMessageCount(roomId: Long): Long =
        txPort.withNewTransaction {
            getMessageCountPort.getMessageCount(roomId)
        }
}

internal class GetMessagesPagingService(
    private val getMessagesPort: GetMessagesPagingPort,
    private val txPort: PersistTransactionPort,
) : GetMessagesPagingUsecase {
    override suspend fun getMessages(roomId: Long, page: Long, pageSize: Int): Collection<MessageEntry> =
        txPort.withNewTransaction {
            getMessagesPort.getMessagesPaging(roomId, page, pageSize)
        }
}

internal class DeleteMessageService(
    private val deleteMessagePort: DeleteMessagePort,
    private val txPort: PersistTransactionPort,
) : DeleteMessageUsecase {
    override suspend fun deleteMessage(messageId: Long) =
        txPort.withNewTransaction {
            deleteMessagePort.deleteMessage(messageId)
        }
}

internal class EditMessageService(
    private val getMessagePort: GetMessagePort,
    private val updateMessagePort: UpdateMessagePort,
    private val txPort: PersistTransactionPort,
) : EditMessageUsecase {
    override suspend fun editMessage(messageId: Long, text: String): MessageEntry =
        txPort.withNewTransaction {
            val message = getMessagePort.getMessage(messageId)
            val editedMessage = editMessage(message, text)
            updateMessagePort.updateMessage(editedMessage)
        }
}

internal class ReadMessageService(
    private val getMessagePort: GetMessagePort,
    private val updateMessagePort: UpdateMessagePort,
    private val txPort: PersistTransactionPort,
) : ReadMessageUsecase {
    override suspend fun readMessage(messageId: Long, applicantId: Long): MessageEntry =
        txPort.withNewTransaction {
            val message = getMessagePort.getMessage(messageId)
            val readMessage = readMessage(message, applicantId)
            updateMessagePort.updateMessage(readMessage)
        }
}