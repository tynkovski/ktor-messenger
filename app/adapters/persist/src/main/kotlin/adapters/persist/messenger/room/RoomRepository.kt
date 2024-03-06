package adapters.persist.messenger.room

import adapters.persist.messenger.entities.*
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

internal class RoomRepository {

    @MustBeCalledInTransactionContext
    fun getByIdOrNull(id: Long): RoomSqlEntity? {
        return RoomSqlEntities
            .select { RoomSqlEntities.id eq id }
            .limit(1)
            .map { RoomSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun hasEntityWithId(id: Long): Boolean {
        return RoomSqlEntities
            .select { RoomSqlEntities.id eq id }
            .limit(1)
            .count() > 0
    }

    @MustBeCalledInTransactionContext
    fun getRoomsPaging(userId: Long, page: Long, pageSize: Int): Collection<RoomSqlEntity> {
        return (RoomSqlEntities innerJoin UserToRoomSqlEntities)
            .select { UserToRoomSqlEntities.userId eq userId }
            .orderBy(RoomSqlEntities.createdAt, SortOrder.ASC)
            .limit(n = pageSize, offset = page * pageSize)
            .map { RoomSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: RoomSqlEntity): RoomSqlEntity {
        return RoomSqlEntities
            .pgInsertOrUpdate(RoomSqlEntities.id) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { RoomSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun deleteById(id: Long): Boolean {
        return RoomSqlEntities.deleteWhere { RoomSqlEntities.id eq id } > 0
    }
}

internal class UserToRoomRepository {
    @MustBeCalledInTransactionContext
    fun getByIdOrNull(id: Long): UserToRoomSqlEntity? {
        return UserToRoomSqlEntities
            .select { UserToRoomSqlEntities.userId eq id }
            .limit(1)
            .map { UserToRoomSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: UserToRoomSqlEntity): UserToRoomSqlEntity {
        return UserToRoomSqlEntities
            .pgInsertOrUpdate(
                UserToRoomSqlEntities.roomId,
                UserToRoomSqlEntities.userId
            ) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { UserToRoomSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun upsert(entities: Collection<UserToRoomSqlEntity>): Collection<UserToRoomSqlEntity> {
        return entities.map { upsert(it) }
    }

    @MustBeCalledInTransactionContext
    fun deleteById(id: Long): Boolean {
        return UserToRoomSqlEntities.deleteWhere { UserToRoomSqlEntities.userId eq id } > 0
    }

    @MustBeCalledInTransactionContext
    fun count(userId: Long): Long {
        return UserToRoomSqlEntities
            .select { UserToRoomSqlEntities.userId eq userId }
            .count()
    }
}

internal class ModeratorToRoomRepository {
    @MustBeCalledInTransactionContext
    fun getByIdOrNull(id: Long): ModeratorToRoomSqlEntity? {
        return ModeratorToRoomSqlEntities
            .select { ModeratorToRoomSqlEntities.userId eq id }
            .limit(1)
            .map { ModeratorToRoomSqlEntity.fromSqlResultRow(it) }
            .singleOrNull()
    }

    @MustBeCalledInTransactionContext
    fun upsert(entity: ModeratorToRoomSqlEntity): ModeratorToRoomSqlEntity {
        return ModeratorToRoomSqlEntities
            .pgInsertOrUpdate(
                ModeratorToRoomSqlEntities.roomId,
                ModeratorToRoomSqlEntities.userId
            ) { entity.toSqlStatement(it) }
            .resultedValues!!
            .first()
            .let { ModeratorToRoomSqlEntity.fromSqlResultRow(it) }
    }

    @MustBeCalledInTransactionContext
    fun upsert(entities: Collection<ModeratorToRoomSqlEntity>): Collection<ModeratorToRoomSqlEntity> {
        return entities.map { upsert(it) }
    }

    @MustBeCalledInTransactionContext
    fun deleteById(id: Long): Boolean {
        return ModeratorToRoomSqlEntities.deleteWhere { ModeratorToRoomSqlEntities.userId eq id } > 0
    }
}