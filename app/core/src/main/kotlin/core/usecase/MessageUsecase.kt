package core.usecase

import core.models.MessageEntry

fun interface AddMessageUsecase {
    suspend fun createMessage(entry: MessageEntry): MessageEntry
}

fun interface GetMessageUsecase {
    suspend fun getMessage(id: Long): MessageEntry
}

fun interface GetMessagesPagingUsecase {
    suspend fun getMessages(roomId: Long, page: Int, pageSize: Int): Collection<MessageEntry>
}

fun interface DeleteMessageUsecase {
    suspend fun deleteMessage(id: Long)
}

fun interface UpdateMessageUsecase {
    suspend fun updateMessage(entry: MessageEntry): MessageEntry
}

fun interface ReadMessageUsecase {
    suspend fun readMessage(userId: Long, entry: MessageEntry): MessageEntry
}
