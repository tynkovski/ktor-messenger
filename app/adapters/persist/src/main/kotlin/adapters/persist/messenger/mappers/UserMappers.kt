package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.UserSqlEntity
import core.models.UserEntry
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


internal fun UserEntry.Companion.fromEntity(
    entity: UserSqlEntity
) = with(entity) {
    UserEntry(
        id = id,
        name = name,
        login = login,
        password = password,
        salt = salt,
        createdAt = createdAt,
    )
}

internal fun UserEntry.toUserSqlEntity() = with(this) {
    UserSqlEntity(
        id = id,
        name = name,
        login = login,
        password = password,
        salt = salt,
        createdAt = createdAt
    )
}