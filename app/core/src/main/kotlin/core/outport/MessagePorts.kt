package core.outport

import core.models.MessageEntry

interface AddMessagePort {
    fun addMessage(entry: MessageEntry): MessageEntry
}

interface GetMessagePort {
    fun getMessage(id: Long): MessageEntry
}

interface GetMessagesPagingPort {
    fun getMessagesPaging(roomId: Long, page: Int, pageSize: Int): Collection<MessageEntry>
}

interface DeleteMessagePort {
    fun deleteMessage(id: Long)
}

interface UpdateMessagePort {
    fun updateMessage(entry: MessageEntry): MessageEntry
}