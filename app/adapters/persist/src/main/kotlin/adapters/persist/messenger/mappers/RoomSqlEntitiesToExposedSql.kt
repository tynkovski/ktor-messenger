package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun RoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[RoomSqlEntities.id] = id }
    it[RoomSqlEntities.name] = name
    it[RoomSqlEntities.image] = image
    it[RoomSqlEntities.createdAt] = createdAt
}

internal fun UserToRoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[UserToRoomSqlEntities.userId] = userId
    it[UserToRoomSqlEntities.roomId] = roomId
}

internal fun ModeratorToRoomSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[ModeratorToRoomSqlEntities.userId] = userId
    it[ModeratorToRoomSqlEntities.roomId] = roomId
}

internal fun RoomSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    RoomSqlEntity(
        id = resultRow[RoomSqlEntities.id],
        name = resultRow[RoomSqlEntities.name],
        image = resultRow[RoomSqlEntities.image],
        createdAt = resultRow[RoomSqlEntities.createdAt]
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
