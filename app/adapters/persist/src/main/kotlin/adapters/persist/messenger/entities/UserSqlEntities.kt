package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

// --- TABLES
internal object UserSqlEntities : Table(name = "user") {
    val id = long("id").autoIncrement()
    val login = text("login").uniqueIndex()
    val password = text("password")
    val salt = text("salt")
    val name = text("name").nullable()
    val image = text("image").nullable()
    val createdAt = datetime("created_at")
    val deletedAt = datetime("deleted_at").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_user_id")
}

internal object ContactToUserEntities : Table(name = "contact_to_user") {
    val userId = long("user_id").references(UserSqlEntities.id)
    val contactId = long("contact_id").references(UserSqlEntities.id)
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(userId, contactId, name = "PK_contact_to_user_id")
}

internal object BlockedToUserEntities : Table(name = "blocked_to_user") {
    val userId = long("user_id").references(UserSqlEntities.id)
    val blockedId = long("blocked_id").references(UserSqlEntities.id)
    val blockedAt = datetime("blocked_at")

    override val primaryKey = PrimaryKey(userId, blockedId, name = "PK_blocked_to_user_id")
}

// --- ENTITIES

data class UserSqlEntity(
    val id: Long? = null,
    val login: String,
    val password: String,
    val salt: String,
    val name: String?,
    val image: String?,
    val createdAt: LocalDateTime,
    val deletedAt: LocalDateTime?
) {
    companion object
}

data class ContactToUserEntity(
    val userId: Long,
    val contactId: Long,
    val createdAt: LocalDateTime
) {
    companion object
}

data class BlockedToUserEntity(
    val userId: Long,
    val blockedId: Long,
    val blockedAt: LocalDateTime
) {
    companion object
}