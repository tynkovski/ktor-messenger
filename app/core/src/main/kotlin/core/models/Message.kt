package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class MessageEntry(
    val id: Long? = null,
    val sender: Long,
    val roomId: Long,
    val text: String,
    val readBy: List<Long>,
    val sentAt: LocalDateTime = LocalDateTime.now()
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
