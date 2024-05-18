package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class RoomEntry(
    val id: Long?,
    val name: String?,
    val image: String?,
    val users: Set<Long>,
    val moderators: Set<Long>,
    val lastAction: LastActionEntry?,
    val deletedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
) {
    data class LastActionEntry(
        val applicantId: Long,
        val actionType: ActionType,
        val description: String?,
        val actionDateTime: LocalDateTime,
    ) {
        enum class ActionType {
            USER_CREATE_ROOM,
            USER_RENAME_ROOM,
            USER_SENT_MESSAGE,
            USER_INVITE_USER,
            USER_KICK_USER,
            USER_QUIT,
            USER_JOINED,
            MAKE_MODERATOR
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
