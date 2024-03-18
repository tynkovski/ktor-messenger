package adapters.persist.messenger.user

import adapters.persist.messenger.entities.BlockedToUserEntities
import adapters.persist.messenger.entities.BlockedToUserEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime

class BlacklistRepository {

    @MustBeCalledInTransactionContext
    fun blockUser(applicantId: Long, userId: Long): Boolean {
        val entity = BlockedToUserEntity(
            userId = applicantId,
            blockedId = userId,
            blockedAt = LocalDateTime.now()
        )
        return BlockedToUserEntities
            .pgInsertOrUpdate(BlockedToUserEntities.userId, BlockedToUserEntities.blockedId) {
                entity.toSqlStatement(it)
            }
            .resultedValues!!.isNotEmpty()
    }


    @MustBeCalledInTransactionContext
    fun unblockUser(applicantId: Long, userId: Long): Boolean {
        return BlockedToUserEntities.deleteWhere {
            (BlockedToUserEntities.userId eq applicantId) and (BlockedToUserEntities.blockedId eq userId)
        } > 0
    }

    @MustBeCalledInTransactionContext
    fun getBlockedUsers(applicantId: Long): Collection<BlockedToUserEntity> {
        return BlockedToUserEntities
            .select { BlockedToUserEntities.userId eq applicantId }
            .map { BlockedToUserEntity.fromSqlResultRow(it) }
    }
}