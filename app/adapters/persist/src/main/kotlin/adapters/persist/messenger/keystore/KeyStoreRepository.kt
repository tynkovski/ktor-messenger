package adapters.persist.messenger.keystore

import adapters.persist.messenger.entities.KeyStoreSqlEntities
import adapters.persist.messenger.entities.KeyStoreSqlEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

internal class KeyStoreRepository {
    @MustBeCalledInTransactionContext
    fun getKeyStoreByAccessKeyOrNull(accessKey: String): KeyStoreSqlEntity? {
        return KeyStoreSqlEntities
            .select {
                KeyStoreSqlEntities.accessKey eq accessKey
            }
            .limit(1)
            .map {
                KeyStoreSqlEntity.fromSqlResultRow(it)
            }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun getKeyStoreByKeysOrNull(accessKey: String, refreshKey: String): KeyStoreSqlEntity? {
        return KeyStoreSqlEntities
            .select {
                (KeyStoreSqlEntities.accessKey eq accessKey) and (KeyStoreSqlEntities.refreshKey eq refreshKey)
            }
            .limit(1)
            .map {
                KeyStoreSqlEntity.fromSqlResultRow(it)
            }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun getKeyStoreByRefreshKeyOrNull(refreshKey: String): KeyStoreSqlEntity? {
        return KeyStoreSqlEntities
            .select {
                KeyStoreSqlEntities.refreshKey eq refreshKey
            }
            .limit(1)
            .map {
                KeyStoreSqlEntity.fromSqlResultRow(it)
            }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: KeyStoreSqlEntity): KeyStoreSqlEntity {
        return KeyStoreSqlEntities
            .pgInsertOrUpdate(KeyStoreSqlEntities.id) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { KeyStoreSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun deleteById(id: Long): Boolean {
        return KeyStoreSqlEntities.deleteWhere { KeyStoreSqlEntities.id eq id } > 0
    }
}