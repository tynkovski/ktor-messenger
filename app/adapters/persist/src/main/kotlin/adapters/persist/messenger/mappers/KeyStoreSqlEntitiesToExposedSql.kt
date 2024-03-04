package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.KeyStoreEntities
import adapters.persist.messenger.entities.KeyStoreSqlEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun KeyStoreSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[KeyStoreEntities.id] = id }
    it[KeyStoreEntities.userId] = userId
    it[KeyStoreEntities.accessKey] = accessKey
    it[KeyStoreEntities.refreshKey] = refreshKey
    it[KeyStoreEntities.createdAt] = createdAt
}

internal fun KeyStoreSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    KeyStoreSqlEntity(
        id = resultRow[KeyStoreEntities.id],
        userId = resultRow[KeyStoreEntities.userId],
        accessKey = resultRow[KeyStoreEntities.accessKey],
        refreshKey = resultRow[KeyStoreEntities.refreshKey],
        createdAt = resultRow[KeyStoreEntities.createdAt],
    )
