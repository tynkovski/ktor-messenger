package adapters.persist.messenger.room

import adapters.persist.messenger.entities.ModeratorToRoomSqlEntity
import adapters.persist.messenger.entities.UserToRoomSqlEntity
import adapters.persist.messenger.mappers.*
import adapters.persist.messenger.mappers.fromEntities
import adapters.persist.messenger.mappers.toSqlEntity
import adapters.persist.messenger.mappers.toUsersToRoomEntities
import core.models.RoomEntry
import core.models.RoomEntryNotFoundException
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
    @MustBeCalledInTransactionContext
    private fun upsertUsers(roomId: Long, roomEntry: RoomEntry): Collection<UserToRoomSqlEntity> {
        val usersToAdd = roomEntry.toUsersToRoomEntities(roomId)

        userToRoomRepository
            .getByIds(roomEntry.users)
            .map(UserToRoomSqlEntity::userId)
            .filterNot(roomEntry.users::contains)
            .forEach(userToRoomRepository::deleteUserFromRoom)

        return userToRoomRepository.upsert(usersToAdd)
    }

    @MustBeCalledInTransactionContext
    private fun upsertModerators(roomId: Long, roomEntry: RoomEntry): Collection<ModeratorToRoomSqlEntity> {
        val moderatorEntities = roomEntry.toModeratorsToRoomEntities(roomId)

        moderatorToRoomRepository
            .getByIds(roomEntry.moderators)
            .map(ModeratorToRoomSqlEntity::userId)
            .filterNot(roomEntry.moderators::contains)
            .forEach(moderatorToRoomRepository::deleteModeratorFromRoom)

        return moderatorToRoomRepository.upsert(moderatorEntities)
    }

    @MustBeCalledInTransactionContext
    private fun upsertRoomEntry(roomEntry: RoomEntry): RoomEntry {
        val roomAdded = roomRepository.upsert(roomEntry.toSqlEntity())
        val roomId = roomAdded.id!!

        val usersAdded = upsertUsers(roomId, roomEntry)
        val moderatorsAdded = upsertModerators(roomId, roomEntry)

        return RoomEntry.fromEntities(roomAdded, usersAdded, moderatorsAdded)
    }

    @MustBeCalledInTransactionContext
    override fun addRoom(entry: RoomEntry): RoomEntry {
        require(entry.id == null) { "entry.id must be null" }
        return upsertRoomEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override fun updateRoom(entry: RoomEntry): RoomEntry {
        val roomId = requireNotNull(entry.id) { "entity.id must not be null" }
        if (!roomRepository.hasEntityWithId(roomId)) {
            throw RoomEntryNotFoundException(searchCriteria = "id=$roomId")
        }
        return upsertRoomEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override fun getRoom(id: Long): RoomEntry {
        val roomEntity = roomRepository.getByIdOrNull(id)
            ?: throw RoomEntryNotFoundException(searchCriteria = "id=$id")

        val usersEntities = userToRoomRepository.getUsers(id)
        val moderatorsEntities = moderatorToRoomRepository.getUsers(id)

        return RoomEntry.fromEntities(
            roomSqlEntity = roomEntity,
            usersToRoomSqlEntities = usersEntities,
            moderatorsToRoomSqlEntities = moderatorsEntities
        )
    }

    @MustBeCalledInTransactionContext
    override fun getRoomsPaging(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry> {
        val roomEntities = roomRepository.getRoomsPaging(userId = userId, page = page, pageSize = pageSize)
        val roomsIds = roomEntities.map { it.id!! }

        val usersEntities = userToRoomRepository.getByIds(roomsIds)
        val moderatorsEntities = moderatorToRoomRepository.getByIds(roomsIds)

        return roomEntities.map { entity ->
            RoomEntry.fromEntities(
                roomSqlEntity = entity,
                usersToRoomSqlEntities = usersEntities,
                moderatorsToRoomSqlEntities = moderatorsEntities
            )
        }
    }

    @MustBeCalledInTransactionContext
    override fun deleteRoom(id: Long) {
        if (!roomRepository.deleteById(id)) {
            throw RoomEntryNotFoundException(searchCriteria = "id=$id")
        }
    }
}