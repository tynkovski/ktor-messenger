package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.*
import adapters.persist.messenger.entities.ContactToUserEntities
import adapters.persist.messenger.entities.ContactToUserEntity
import adapters.persist.messenger.entities.UserSqlEntities
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun UserSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[UserSqlEntities.id] = id }
    it[UserSqlEntities.name] = name
    it[UserSqlEntities.login] = login
    it[UserSqlEntities.password] = password
    it[UserSqlEntities.salt] = salt
    it[UserSqlEntities.createdAt] = createdAt
    it[UserSqlEntities.image] = image
}

internal fun ContactToUserEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
     it[ContactToUserEntities.userId] = userId
     it[ContactToUserEntities.contactId] = contactId
     it[ContactToUserEntities.createdAt] = createdAt
}

internal fun BlockedToUserEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
     it[BlockedToUserEntities.userId] = userId
     it[BlockedToUserEntities.blockedId] = blockedId
     it[BlockedToUserEntities.blockedAt] = blockedAt
}

internal fun UserSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    UserSqlEntity(
        id = resultRow[UserSqlEntities.id],
        name = resultRow[UserSqlEntities.name],
        login = resultRow[UserSqlEntities.login],
        password = resultRow[UserSqlEntities.password],
        salt = resultRow[UserSqlEntities.salt],
        createdAt = resultRow[UserSqlEntities.createdAt],
        deletedAt = resultRow[UserSqlEntities.deletedAt],
        image = resultRow[UserSqlEntities.image],
    )

internal fun ContactToUserEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    ContactToUserEntity(
        userId = resultRow[ContactToUserEntities.userId],
        contactId = resultRow[ContactToUserEntities.contactId],
        createdAt = resultRow[ContactToUserEntities.createdAt],
    )

internal fun BlockedToUserEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    BlockedToUserEntity(
        userId = resultRow[BlockedToUserEntities.userId],
        blockedId = resultRow[BlockedToUserEntities.blockedId],
        blockedAt = resultRow[BlockedToUserEntities.blockedAt],
    )

