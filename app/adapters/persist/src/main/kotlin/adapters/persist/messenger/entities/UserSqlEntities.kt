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

// --- ENTITIES

internal data class UserSqlEntity(
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