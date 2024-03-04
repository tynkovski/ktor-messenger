package adapters.persist.messenger.keystore

import adapters.persist.messenger.entities.KeyStoreEntities
import adapters.persist.messenger.entities.KeyStoreSqlEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.select

internal class KeyStoreRepository {
    @MustBeCalledInTransactionContext

    fun getByAccessKeyOrNull(accessKey: String): KeyStoreSqlEntity? {
        return KeyStoreEntities
            .select {
                KeyStoreEntities.accessKey eq accessKey
            }
            .limit(1)
            .map {
                KeyStoreSqlEntity.fromSqlResultRow(it)
            }
            .singleOrNull()
    }
}