package core.services

import core.models.MessageEntry
import core.outport.*
import core.usecase.*

internal class AddMessageService(
    private val addMessagePort: AddMessagePort,
    private val txPort: PersistTransactionPort,
) : AddMessageUsecase {
    override suspend fun createMessage(entry: MessageEntry): MessageEntry =
        txPort.withNewTransaction {
            addMessagePort.addMessage(entry)
        }
}

internal class GetMessageService(
    private val getMessagePort: GetMessagePort,
    private val txPort: PersistTransactionPort,
) : GetMessageUsecase {
    override suspend fun getMessage(id: Long): MessageEntry =
        txPort.withNewTransaction {
            getMessagePort.getMessage(id)
        }
}

internal class GetMessagesPagingService(
    private val getMessagesPort: GetMessagesPagingPort,
    private val txPort: PersistTransactionPort,
) : GetMessagesPagingUsecase {
    override suspend fun getMessages(roomId: Long, page: Int, pageSize: Int): Collection<MessageEntry> =
        txPort.withNewTransaction {
            getMessagesPort.getMessagesPaging(roomId, page, pageSize)
        }
}

internal class DeleteMessageService(
    private val deleteMessagePort: DeleteMessagePort,
    private val txPort: PersistTransactionPort,
) : DeleteMessageUsecase {
    override suspend fun deleteMessage(id: Long) =
        txPort.withNewTransaction {
            deleteMessagePort.deleteMessage(id)
        }
}

internal class UpdateMessageService(
    private val updateMessagePort: UpdateMessagePort,
    private val txPort: PersistTransactionPort,
) : UpdateMessageUsecase {
    override suspend fun updateMessage(entry: MessageEntry): MessageEntry =
        txPort.withNewTransaction {
            updateMessagePort.updateMessage(entry)
        }
}

internal class ReadMessageService(
    private val updateMessagePort: UpdateMessagePort,
    private val txPort: PersistTransactionPort,
) : ReadMessageUsecase {
    override suspend fun readMessage(userId: Long, entry: MessageEntry): MessageEntry =
        txPort.withNewTransaction {
            val newReadBy = entry.readBy + userId
            val newMessage = entry.copy(readBy = newReadBy)
            updateMessagePort.updateMessage(newMessage)
        }
}