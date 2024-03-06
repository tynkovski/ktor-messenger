package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

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

internal data class RoomSqlEntity(
    val id: Long?,
    val name: String?,
    val image: String?,
    val createdAt: LocalDateTime
) {
    companion object
}

internal data class UserToRoomSqlEntity(
    val userId: Long,
    val roomId: Long,
) {
    companion object
}

internal data class ModeratorToRoomSqlEntity(
    val userId: Long,
    val roomId: Long,
) {
    companion object
}