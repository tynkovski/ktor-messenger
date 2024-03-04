package adapters.persist.messenger.user

import adapters.persist.messenger.entities.UserSqlEntities
import adapters.persist.messenger.entities.UserSqlEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

internal class UserRepository {
    @MustBeCalledInTransactionContext
    fun getByIdOrNull(id: Long): UserSqlEntity? {
        return UserSqlEntities
            .select { UserSqlEntities.id eq id }
            .limit(1)
            .map { UserSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun getByLoginOrNull(login: String): UserSqlEntity? {
        return UserSqlEntities
            .select { UserSqlEntities.login eq login }
            .limit(1)
            .map { UserSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: UserSqlEntity): UserSqlEntity {
        return UserSqlEntities
            .pgInsertOrUpdate(UserSqlEntities.id) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { UserSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun deleteById(id: Long): Boolean {
        return UserSqlEntities.deleteWhere { UserSqlEntities.id eq id } > 0
    }

    @MustBeCalledInTransactionContext
    fun hasEntityWithId(id: Long): Boolean {
        return UserSqlEntities
            .select { UserSqlEntities.id eq id }
            .limit(1)
            .count() > 0
    }
}