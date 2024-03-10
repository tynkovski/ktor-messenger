package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

internal object MessageSqlEntities : Table(name = "message") {
    val id = long("id").autoIncrement()
    val roomId = long("room_id").references(RoomSqlEntities.id)
    val senderId = long("sender_id").references(UserSqlEntities.id)
    val text = text("text")
    val createdAt = datetime("created_at")
    val editedAt = datetime("edited_at").nullable()
    val deletedAt = datetime("deleted_at").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_message_id")
}

internal object ReaderToMessageSqlEntities : Table(name = "user_to_message") {
    val messageId = long("message_id")
        .references(MessageSqlEntities.id, onDelete = ReferenceOption.CASCADE)
    val readerId = long("reader_id").references(UserSqlEntities.id)

    override val primaryKey = PrimaryKey(messageId, readerId, name = "PK_reader_to_message_id")
}

internal data class MessageSqlEntity(
    val id: Long?,
    val senderId: Long,
    val roomId: Long,
    val text: String,
    val sentAt: LocalDateTime,
    val editedAt: LocalDateTime?,
    val deletedAt: LocalDateTime?
) {
    companion object
}

internal data class ReaderToMessageSqlEntity(
    val readerId: Long,
    val messageId: Long,
) {
    companion object
}