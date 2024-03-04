package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.KeyStoreSqlEntity
import core.models.KeyStoreEntry

internal fun KeyStoreEntry.Companion.fromEntity(
    entity: KeyStoreSqlEntity?
) = with(entity) {
    this?.let {
        KeyStoreEntry(
            id = id,
            userId = userId,
            accessKey = accessKey,
            refreshKey = refreshKey,
            createdAt = createdAt,
        )
    }
}

internal fun KeyStoreEntry.toUserSqlEntity() = with(this) {
    KeyStoreSqlEntity(
        id = id,
        userId = userId,
        accessKey = accessKey,
        refreshKey = refreshKey,
        createdAt = createdAt,
    )
}