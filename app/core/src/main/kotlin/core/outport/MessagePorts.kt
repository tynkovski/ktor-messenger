package core.outport

import core.models.MessageEntry

interface AddMessagePort {
    fun addMessage(entry: MessageEntry): MessageEntry
}

interface GetMessagePort {
    fun getMessage(messageId: Long): MessageEntry
}

interface GetMessageCountPort {
    fun getMessageCount(roomId: Long): Long
}

interface GetMessagesPagingPort {
    fun getMessagesPaging(roomId: Long, page: Long, pageSize: Int): Collection<MessageEntry>
}

interface DeleteMessagePort {
    fun deleteMessage(messageId: Long)
}

interface UpdateMessagePort {
    fun updateMessage(entry: MessageEntry): MessageEntry
}