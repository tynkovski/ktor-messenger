package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.MessageSqlEntities
import adapters.persist.messenger.entities.MessageSqlEntity
import adapters.persist.messenger.entities.ReaderToMessageSqlEntities
import adapters.persist.messenger.entities.ReaderToMessageSqlEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun MessageSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[MessageSqlEntities.id] = id }
    it[MessageSqlEntities.senderId] = senderId
    it[MessageSqlEntities.roomId] = roomId
    it[MessageSqlEntities.text] = text
    it[MessageSqlEntities.createdAt] = sentAt
    it[MessageSqlEntities.editedAt] = editedAt
    it[MessageSqlEntities.deletedAt] = deletedAt
}

internal fun ReaderToMessageSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    it[ReaderToMessageSqlEntities.readerId] = readerId
    it[ReaderToMessageSqlEntities.messageId] = messageId
}

internal fun MessageSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    MessageSqlEntity(
        id = resultRow[MessageSqlEntities.id],
        senderId = resultRow[MessageSqlEntities.senderId],
        roomId = resultRow[MessageSqlEntities.roomId],
        text = resultRow[MessageSqlEntities.text],
        sentAt = resultRow[MessageSqlEntities.createdAt],
        editedAt = resultRow[MessageSqlEntities.editedAt],
        deletedAt = resultRow[MessageSqlEntities.deletedAt]
    )

internal fun ReaderToMessageSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    ReaderToMessageSqlEntity(
        messageId = resultRow[ReaderToMessageSqlEntities.messageId],
        readerId = resultRow[ReaderToMessageSqlEntities.readerId],
    )