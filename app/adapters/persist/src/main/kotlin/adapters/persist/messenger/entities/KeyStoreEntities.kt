package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

internal object KeyStoreEntities : Table(name = "key_store") {
    val id = long("id").autoIncrement()
    val userId = long("user_id")
        .references(PersonSqlEntities.id, onDelete = ReferenceOption.CASCADE)
        .uniqueIndex()
    val accessKey = text("access_key").index()
    val refreshKey = text("refresh_key")
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(UserSqlEntities.id, name = "PK_user_id")
}

internal data class KeyStoreSqlEntity(
    val id: Long? = null,
    val userId: Long,
    val accessKey: String,
    val refreshKey: String,
    val createdAt: LocalDateTime
) {
    companion object
}