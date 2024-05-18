package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun RoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[RoomSqlEntities.id] = id }
    it[RoomSqlEntities.name] = name
    it[RoomSqlEntities.image] = image
    it[RoomSqlEntities.createdAt] = createdAt
    it[RoomSqlEntities.deletedAt] = deletedAt
}

internal fun UserToRoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[UserToRoomSqlEntities.userId] = userId
    it[UserToRoomSqlEntities.roomId] = roomId
}

internal fun ModeratorToRoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[ModeratorToRoomSqlEntities.userId] = userId
    it[ModeratorToRoomSqlEntities.roomId] = roomId
}

internal fun ActionToRoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[ActionToRoomSqlEntities.roomId] = roomId
    it[ActionToRoomSqlEntities.applicantId] = applicantId
    it[ActionToRoomSqlEntities.description] = description
    it[ActionToRoomSqlEntities.actionDateTime] = actionDateTime
    it[ActionToRoomSqlEntities.actionType] = actionType
}

internal fun RoomSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    RoomSqlEntity(
        id = resultRow[RoomSqlEntities.id],
        name = resultRow[RoomSqlEntities.name],
        image = resultRow[RoomSqlEntities.image],
        createdAt = resultRow[RoomSqlEntities.createdAt],
        deletedAt = resultRow[RoomSqlEntities.deletedAt]
    )


internal fun UserToRoomSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    UserToRoomSqlEntity(
        userId = resultRow[UserToRoomSqlEntities.userId],
        roomId = resultRow[UserToRoomSqlEntities.roomId]
    )


internal fun ModeratorToRoomSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    ModeratorToRoomSqlEntity(
        userId = resultRow[ModeratorToRoomSqlEntities.userId],
        roomId = resultRow[ModeratorToRoomSqlEntities.roomId]
    )

internal fun ActionToRoomSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    ActionToRoomSqlEntity(
        roomId = resultRow[ActionToRoomSqlEntities.roomId],
        applicantId = resultRow[ActionToRoomSqlEntities.applicantId],
        description = resultRow[ActionToRoomSqlEntities.description],
        actionDateTime = resultRow[ActionToRoomSqlEntities.actionDateTime],
        actionType = resultRow[ActionToRoomSqlEntities.actionType],
    )
