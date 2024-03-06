package adapters.persist.messenger.room

import adapters.persist.messenger.mappers.fromEntities
import adapters.persist.messenger.mappers.toSqlEntities
import adapters.persist.messenger.mappers.toSqlEntity
import com.github.michaelbull.logging.InlineLogger
import core.models.RoomEntry
import core.outport.*

internal class RoomAdapter(
    private val roomRepository: RoomRepository,
    private val userToRoomRepository: UserToRoomRepository,
    private val moderatorToRoomRepository: ModeratorToRoomRepository,
) : AddRoomPort,
    GetRoomPort,
    GetRoomsPagingPort,
    UpdateRoomPort,
    DeleteRoomPort {

    private val logger = InlineLogger()

    @MustBeCalledInTransactionContext
    override fun addRoom(entry: RoomEntry): RoomEntry {
        val roomEntity = roomRepository.upsert(entry.toSqlEntity())
        val (usersEntities, moderatorsEntities) = entry.toSqlEntities(roomEntity.id!!)
        val usersEntitiesNew = userToRoomRepository.upsert(usersEntities)
        val moderatorsEntitiesNew = moderatorToRoomRepository.upsert(moderatorsEntities)
        return RoomEntry.fromEntities(roomEntity, usersEntitiesNew, moderatorsEntitiesNew)
    }

    @MustBeCalledInTransactionContext
    override fun getRoom(id: Long): RoomEntry {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun getRoomsPaging(userId: Long, page: Int, pageSize: Int): Collection<RoomEntry> {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun deleteRoom(id: Long) {
        TODO("Not yet implemented")
    }

    @MustBeCalledInTransactionContext
    override fun updateRoom(entry: RoomEntry): RoomEntry {
        TODO("Not yet implemented")
    }
}