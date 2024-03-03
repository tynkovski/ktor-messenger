package adapters.persist.messenger.mappers

import adapters.persist.messenger.entities.UserSqlEntities
import adapters.persist.messenger.entities.UserSqlEntity
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

internal fun UserSqlEntity.toSqlStatement(statement: InsertStatement<Number>) = statement.let {
    id?.let { id -> it[UserSqlEntities.id] = id }
    it[UserSqlEntities.name] = name
    it[UserSqlEntities.login] = login
    it[UserSqlEntities.password] = password
    it[UserSqlEntities.salt] = salt
    it[UserSqlEntities.createdAt] = createdAt
}

internal fun UserSqlEntity.Companion.fromSqlResultRow(resultRow: ResultRow) =
    UserSqlEntity(
        id = resultRow[UserSqlEntities.id],
        name = resultRow[UserSqlEntities.name],
        login = resultRow[UserSqlEntities.login],
        password = resultRow[UserSqlEntities.password],
        salt = resultRow[UserSqlEntities.salt],
        createdAt = resultRow[UserSqlEntities.createdAt],
    )
