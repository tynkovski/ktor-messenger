package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class MessageEntry(
    val id: Long?,
    val senderId: Long,
    val roomId: Long,
    val text: String,
    val readBy: Set<Long>,
    val editedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?,
    val sentAt: LocalDateTime
) {
    companion object
}

class MessageEntryNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Message not found",
    detail = "Message not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)
