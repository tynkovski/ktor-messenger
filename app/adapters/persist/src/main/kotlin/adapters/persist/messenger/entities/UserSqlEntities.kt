package adapters.persist.messenger.entities

import adapters.persist.messenger.entities.PersonSqlEntities.index
import adapters.persist.messenger.entities.PersonSqlEntities.uniqueIndex
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

// --- TABLES
internal object UserSqlEntities : Table(name = "user") {
    val id = long("id").autoIncrement()
    val name = text("name")
    val login = text("login").uniqueIndex()
    val password = text("password")
    val salt = text("salt")
    val createdAt = datetime("created_at").index()

    override val primaryKey = PrimaryKey(id, name = "PK_user_id")

}

// --- ENTITIES

internal data class UserSqlEntity(
    val id: Long? = null,
    val name: String,
    val login: String,
    val password: String,
    val salt: String,
    val createdAt: LocalDateTime
) {
    companion object
}