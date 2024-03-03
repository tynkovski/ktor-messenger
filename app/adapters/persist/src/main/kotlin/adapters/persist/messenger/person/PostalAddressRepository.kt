package adapters.persist.messenger.person

import adapters.persist.messenger.entities.PostalAddressSqlEntities
import adapters.persist.messenger.entities.PostalAddressSqlEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import com.github.michaelbull.logging.InlineLogger
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

internal class PostalAddressRepository {

    private val logger = InlineLogger()

    @MustBeCalledInTransactionContext
    fun getById(id: Long): PostalAddressSqlEntity {
        return PostalAddressSqlEntities
            .select {
                PostalAddressSqlEntities.id eq id
            }
            .limit(1)
            .map {
                PostalAddressSqlEntity.fromSqlResultRow(it)
            }
            .single()
    }

    @MustBeCalledInTransactionContext
    fun getByPersonIdOrNull(id: Long): PostalAddressSqlEntity? {
        return PostalAddressSqlEntities
            .select {
                PostalAddressSqlEntities.personId eq id
            }
            .limit(1)
            .map {
                PostalAddressSqlEntity.fromSqlResultRow(it)
            }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: PostalAddressSqlEntity): PostalAddressSqlEntity {
        logger.debug { "upsert(): Update/insert $entity" }
        return PostalAddressSqlEntities
            .pgInsertOrUpdate(PostalAddressSqlEntities.id) {
                entity.toSqlStatement(it)
            }
            .resultedValues!!
            .first()
            .let {
                PostalAddressSqlEntity.fromSqlResultRow(it)
            }
    }

    @MustBeCalledInTransactionContext
    fun getAll(): List<PostalAddressSqlEntity> {
        return PostalAddressSqlEntities
            .selectAll()
            .map {
                PostalAddressSqlEntity.fromSqlResultRow(it)
            }
    }
}
