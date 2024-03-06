package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class RoomEntry(
    val id: Long? = null,
    val name: String?,
    val image: String?,
    val users: List<Long>,
    val moderators: List<Long>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object
}

class RoomEntryNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Room not found",
    detail = "Room not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)
