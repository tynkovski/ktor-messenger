package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.MessageSqlEntity
import adapters.persist.messenger.entities.ReaderToMessageSqlEntity
import core.models.MessageEntry

internal fun MessageEntry.Companion.fromEntities(
    messageSqlEntity: MessageSqlEntity,
    readersToMessageSqlEntity: Collection<ReaderToMessageSqlEntity>
) = with(messageSqlEntity) {
    MessageEntry(
        id = id,
        senderId = senderId,
        roomId = roomId,
        text = text,
        readBy = readersToMessageSqlEntity.map { it.readerId }.toSet(),
        sentAt = sentAt,
        editedAt = editedAt
    )
}

internal fun MessageEntry.toSqlEntity() = with(this) {
    MessageSqlEntity(
        id = id,
        senderId = senderId,
        roomId = roomId,
        text = text,
        sentAt = sentAt,
        editedAt = editedAt
    )
}

internal fun MessageEntry.toReadersSqlEntities(messageId: Long) = with(this.readBy) {
    map { readerId ->
        ReaderToMessageSqlEntity(
            readerId = readerId,
            messageId = messageId
        )
    }
}
