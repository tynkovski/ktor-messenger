package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

internal object MessageSqlEntities : Table(name = "message") {
    val id = long("id").autoIncrement()

    // todo add user status deleted.
    // trouble: messages are deleted after user is deleted

    val senderId = long("sender_id")
        .references(UserSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val roomId = long("room_id")
        .references(RoomSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val text = text("text")

    val sentAt = datetime("created_at")

    val editedAt = datetime("edited_at").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_message_id")
}

internal object ReaderToMessageSqlEntities : Table(name = "user_to_message") {
    val readerId = long("reader_id")
        .references(UserSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val messageId = long("message_id")
        .references(MessageSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(messageId, readerId, name = "PK_reader_to_message_id")
}

internal data class MessageSqlEntity(
    val id: Long?,
    val senderId: Long,
    val roomId: Long,
    val text: String,
    val sentAt: LocalDateTime,
    val editedAt: LocalDateTime?
) {
    companion object
}

internal data class ReaderToMessageSqlEntity(
    val readerId: Long,
    val messageId: Long,
) {
    companion object
}