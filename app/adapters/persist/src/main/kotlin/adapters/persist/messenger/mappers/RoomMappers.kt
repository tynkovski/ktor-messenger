package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.*
import core.models.RoomEntry

internal fun RoomEntry.Companion.fromEntities(
    roomSqlEntity: RoomSqlEntity,
    usersToRoomSqlEntities: Collection<UserToRoomSqlEntity>,
    moderatorsToRoomSqlEntities: Collection<ModeratorToRoomSqlEntity>,
    actionToRoomSqlEntity: ActionToRoomSqlEntity,
) = with(roomSqlEntity) {
    RoomEntry(
        id = id,
        name = name,
        image = image,
        users = usersToRoomSqlEntities.map { it.userId }.toSet(),
        moderators = moderatorsToRoomSqlEntities.map { it.userId }.toSet(),
        createdAt = createdAt,
        lastAction = RoomEntry.LastActionEntry.fromActionSqlEntity(actionToRoomSqlEntity)
    )
}

internal fun RoomEntry.LastActionEntry.Companion.fromActionSqlEntity(actionEntity: ActionSqlType) =
    when (actionEntity) {
        ActionSqlType.USER_CREATE_ROOM -> RoomEntry.LastActionEntry.ActionType.USER_CREATE_ROOM
        ActionSqlType.USER_RENAME_ROOM -> RoomEntry.LastActionEntry.ActionType.USER_RENAME_ROOM
        ActionSqlType.USER_SENT_MESSAGE -> RoomEntry.LastActionEntry.ActionType.USER_SENT_MESSAGE
        ActionSqlType.USER_INVITE_USER -> RoomEntry.LastActionEntry.ActionType.USER_INVITE_USER
        ActionSqlType.USER_KICK_USER -> RoomEntry.LastActionEntry.ActionType.USER_KICK_USER
        ActionSqlType.USER_QUIT -> RoomEntry.LastActionEntry.ActionType.USER_QUIT
        ActionSqlType.USER_JOINED -> RoomEntry.LastActionEntry.ActionType.USER_JOINED
        ActionSqlType.MAKE_MODERATOR -> RoomEntry.LastActionEntry.ActionType.MAKE_MODERATOR
    }

internal fun RoomEntry.LastActionEntry.Companion.fromActionSqlEntity(entity: ActionToRoomSqlEntity) =
    with(entity) {
        RoomEntry.LastActionEntry(
            applicantId = applicantId,
            description = description,
            actionDateTime = actionDateTime,
            actionType = RoomEntry.LastActionEntry.fromActionSqlEntity(actionType)
        )
    }

internal fun RoomEntry.toSqlEntity() = with(this) {
    RoomSqlEntity(
        id = id,
        name = name,
        image = image,
        createdAt = createdAt
    )
}

internal fun RoomEntry.LastActionEntry.ActionType.toSqlEntity() = when (this) {
    RoomEntry.LastActionEntry.ActionType.USER_CREATE_ROOM -> ActionSqlType.USER_CREATE_ROOM
    RoomEntry.LastActionEntry.ActionType.USER_RENAME_ROOM -> ActionSqlType.USER_RENAME_ROOM
    RoomEntry.LastActionEntry.ActionType.USER_SENT_MESSAGE -> ActionSqlType.USER_SENT_MESSAGE
    RoomEntry.LastActionEntry.ActionType.USER_INVITE_USER -> ActionSqlType.USER_INVITE_USER
    RoomEntry.LastActionEntry.ActionType.USER_KICK_USER -> ActionSqlType.USER_KICK_USER
    RoomEntry.LastActionEntry.ActionType.USER_QUIT -> ActionSqlType.USER_QUIT
    RoomEntry.LastActionEntry.ActionType.USER_JOINED -> ActionSqlType.USER_JOINED
    RoomEntry.LastActionEntry.ActionType.MAKE_MODERATOR -> ActionSqlType.MAKE_MODERATOR
}

internal fun RoomEntry.LastActionEntry.toActionToRoomEntity(roomId: Long) = with(this) {
    ActionToRoomSqlEntity(
        roomId = roomId,
        applicantId = applicantId,
        description = description,
        actionDateTime = actionDateTime,
        actionType = actionType.toSqlEntity()
    )
}

internal fun RoomEntry.toUsersToRoomEntities(roomId: Long) = with(this.users) {
    map { userId ->
        UserToRoomSqlEntity(
            userId = userId,
            roomId = roomId
        )
    }
}

internal fun RoomEntry.toModeratorsToRoomEntities(roomId: Long) = with(this.moderators) {
    map { userId ->
        ModeratorToRoomSqlEntity(
            userId = userId,
            roomId = roomId
        )
    }
}
