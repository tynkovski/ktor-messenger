package adapters.persist.messenger.message

import com.github.michaelbull.logging.InlineLogger
import core.models.MessageEntry
import core.outport.*

internal class MessageAdapter(
    private val messageRepository: MessageRepository
) : AddMessagePort,
    GetMessagePort,
    GetMessagesPagingPort,
    UpdateMessagePort,
    DeleteMessagePort {

    private val logger = InlineLogger()

    @MustBeCalledInTransactionContext
    override fun addMessage(entry: MessageEntry): MessageEntry {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun getMessage(id: Long): MessageEntry {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun getMessagesPaging(roomId: Long, page: Int, pageSize: Int): Collection<MessageEntry> {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun deleteMessage(id: Long) {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun updateMessage(entry: MessageEntry): MessageEntry {
        TODO("Not yet implemented")
    }
}