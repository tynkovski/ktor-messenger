package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class RoomEntry(
    val id: Long? = null,
    val name: String?,
    val image: String?,
    val lastAction: LastActionEntry,
    val users: Set<Long>,
    val moderators: Set<Long>,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    data class LastActionEntry(
        val authorId: Long,
        val actionType: ActionType,
        val description: String?,
        val actionDateTime: LocalDateTime = LocalDateTime.now(),
    ) {
        enum class ActionType {
            USER_CREATE_ROOM, USER_SENT_MESSAGE, USER_INVITE_USER, USER_KICK_USER, USER_QUIT, USER_JOINED
        }

        companion object
    }

    companion object
}

class RoomEntryNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Room not found",
    detail = "Room not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)

class LastActionNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Last action of room not found",
    detail = "Last action of room not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)
