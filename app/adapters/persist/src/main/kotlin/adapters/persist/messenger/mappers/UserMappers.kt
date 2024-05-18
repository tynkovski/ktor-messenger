package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.UserSqlEntity
import core.models.UserEntry

internal fun UserEntry.Companion.fromEntity(
    entity: UserSqlEntity
) = with(entity) {
    UserEntry(
        id = id,
        login = login,
        password = password,
        salt = salt,
        name = name,
        image = image,
        createdAt = createdAt,
        deletedAt = deletedAt,
    )
}

internal fun UserEntry.toUserSqlEntity() = with(this) {
    UserSqlEntity(
        id = id,
        login = login,
        password = password,
        salt = salt,
        name = name,
        image = image,
        createdAt = createdAt,
        deletedAt = deletedAt,
    )
}