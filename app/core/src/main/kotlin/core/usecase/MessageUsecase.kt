package core.usecase

import core.models.MessageEntry

fun interface AddMessageUsecase {
    suspend fun createMessage(
        applicantId: Long,
        roomId: Long,
        text: String
    ): MessageEntry
}

fun interface GetMessageUsecase {
    suspend fun getMessage(messageId: Long): MessageEntry
}

fun interface GetMessageCountUsecase {
    suspend fun getMessageCount(roomId: Long): Long
}

fun interface GetMessagesPagingUsecase {
    suspend fun getMessages(roomId: Long, page: Long, pageSize: Int): Collection<MessageEntry>
}

fun interface DeleteMessageUsecase {
    suspend fun deleteMessage(messageId: Long)
}

fun interface EditMessageUsecase {
    suspend fun editMessage(messageId: Long, text: String): MessageEntry
}

fun interface ReadMessageUsecase {
    suspend fun readMessage(messageId: Long, applicantId: Long): MessageEntry
}