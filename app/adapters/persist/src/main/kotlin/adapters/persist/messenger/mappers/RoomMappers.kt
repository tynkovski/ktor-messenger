package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.ModeratorToRoomSqlEntity
import adapters.persist.messenger.entities.RoomSqlEntity
import adapters.persist.messenger.entities.UserToRoomSqlEntity
import core.models.RoomEntry

internal fun RoomEntry.Companion.fromEntities(
    roomSqlEntity: RoomSqlEntity,
    usersToRoomSqlEntities: Collection<UserToRoomSqlEntity>,
    moderatorsToRoomSqlEntities: Collection<ModeratorToRoomSqlEntity>,
) = with(roomSqlEntity) {
    RoomEntry(
        id = id,
        name = name,
        image = image,
        users = usersToRoomSqlEntities.map { it.userId }.toSet(),
        moderators = moderatorsToRoomSqlEntities.map { it.userId }.toSet(),
        createdAt = createdAt
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