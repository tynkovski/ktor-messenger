package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

internal enum class ActionSqlType {
    USER_CREATE_ROOM,
    USER_RENAME_ROOM,
    USER_SENT_MESSAGE,
    USER_INVITE_USER,
    USER_KICK_USER,
    USER_QUIT,
    USER_JOINED,
    MAKE_MODERATOR,
}

internal object RoomSqlEntities : Table(name = "room") {
    val id = long("id").autoIncrement()
    val name = text("name").nullable()
    val image = text("image").nullable()
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id, name = "PK_room_id")
}

internal object UserToRoomSqlEntities : Table(name = "user_to_room") {
    val userId = long("user_id")
        .references(UserSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val roomId = long("room_id")
        .references(RoomSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(roomId, userId, name = "PK_user_to_room_id")
}

internal object ModeratorToRoomSqlEntities : Table(name = "moderator_to_room") {
    val userId = long("user_id")
        .references(UserSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val roomId = long("room_id")
        .references(RoomSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(roomId, userId, name = "PK_moderator_to_room_id")
}

internal object ActionToRoomSqlEntities : Table(name = "action_to_room") {
    val roomId = long("room_id")
        .references(RoomSqlEntities.id, onDelete = ReferenceOption.CASCADE)

    val applicantId = long("author_id")
        .references(UserSqlEntities.id)

    val description = text("action_description").nullable()

    val actionType = enumerationByName("action_type", 20, ActionSqlType::class)

    val actionDateTime = datetime("action_datetime").index()

    override val primaryKey = PrimaryKey(roomId)
}

internal data class RoomSqlEntity(
    val id: Long?,
    val name: String?,
    val image: String?,
    val createdAt: LocalDateTime
) {
    companion object
}

internal data class UserToRoomSqlEntity(
    val roomId: Long,
    val userId: Long,
) {
    companion object
}

internal data class ModeratorToRoomSqlEntity(
    val roomId: Long,
    val userId: Long,
) {
    companion object
}

internal data class ActionToRoomSqlEntity(
    val roomId: Long,
    val applicantId: Long,
    val description: String?,
    val actionType: ActionSqlType,
    val actionDateTime: LocalDateTime
) {
    companion object
}