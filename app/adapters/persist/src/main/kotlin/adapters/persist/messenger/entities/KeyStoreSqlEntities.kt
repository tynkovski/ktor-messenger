package adapters.persist.messenger.entities

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

internal object KeyStoreSqlEntities : Table(name = "key_store") {
    val id = long("id").autoIncrement()
    val userId = long("user_id")
        .references(UserSqlEntities.id, onDelete = ReferenceOption.CASCADE)
        .index()
    val accessKey = text("access_key").uniqueIndex()
    val refreshKey = text("refresh_key").uniqueIndex()
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id, name = "PK_key_store_id")
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