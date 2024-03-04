package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.KeyStoreSqlEntities
import adapters.persist.messenger.entities.KeyStoreSqlEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun KeyStoreSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[KeyStoreSqlEntities.id] = id }
    it[KeyStoreSqlEntities.userId] = userId
    it[KeyStoreSqlEntities.accessKey] = accessKey
    it[KeyStoreSqlEntities.refreshKey] = refreshKey
    it[KeyStoreSqlEntities.createdAt] = createdAt
}

internal fun KeyStoreSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    KeyStoreSqlEntity(
        id = resultRow[KeyStoreSqlEntities.id],
        userId = resultRow[KeyStoreSqlEntities.userId],
        accessKey = resultRow[KeyStoreSqlEntities.accessKey],
        refreshKey = resultRow[KeyStoreSqlEntities.refreshKey],
        createdAt = resultRow[KeyStoreSqlEntities.createdAt],
    )
