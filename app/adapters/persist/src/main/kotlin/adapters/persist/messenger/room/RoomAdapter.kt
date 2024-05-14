package adapters.persist.messenger.room

import adapters.persist.messenger.entities.ActionToRoomSqlEntity
import adapters.persist.messenger.entities.ModeratorToRoomSqlEntity
import adapters.persist.messenger.entities.UserToRoomSqlEntity
import adapters.persist.messenger.mappers.*
import core.models.LastActionNotFoundException
import core.models.RoomEntry
import core.models.RoomEntryNotFoundException
import core.outport.*

internal class RoomAdapter(
    private val roomRepository: RoomRepository,
    private val userToRoomRepository: UserToRoomRepository,
    private val moderatorToRoomRepository: ModeratorToRoomRepository,
    private val actionToRoomRepository: ActionToRoomRepository,
) : AddRoomPort,
    GetRoomPort,
    FindRoomPort,
    GetRoomCountPort,
    GetRoomsPagingPort,
    UpdateRoomPort,
    DeleteRoomPort {

    @MustBeCalledInTransactionContext
    private fun upsertUsers(roomId: Long, roomEntry: RoomEntry): Collection<UserToRoomSqlEntity> {
        val usersToAdd = roomEntry.toUsersToRoomEntities(roomId)

        userToRoomRepository
            .getUsersByRoomId(roomId)
            .map(UserToRoomSqlEntity::userId)
            .filterNot(roomEntry.users::contains)
            .forEach(userToRoomRepository::deleteUserFromRoom)

        return userToRoomRepository.upsert(usersToAdd)
    }

    @MustBeCalledInTransactionContext
    private fun upsertModerators(roomId: Long, roomEntry: RoomEntry): Collection<ModeratorToRoomSqlEntity> {
        val moderatorEntities = roomEntry.toModeratorsToRoomEntities(roomId)

        moderatorToRoomRepository
            .getUsersByRoomId(roomId)
            .map(ModeratorToRoomSqlEntity::userId)
            .filterNot(roomEntry.moderators::contains)
            .forEach(moderatorToRoomRepository::deleteModeratorFromRoom)

        return moderatorToRoomRepository.upsert(moderatorEntities)
    }

    @MustBeCalledInTransactionContext
    private fun upsertLastAction(roomId: Long, action: RoomEntry.LastActionEntry): ActionToRoomSqlEntity? {
        val actionEntity = action.toActionToRoomEntity(roomId)
        return actionToRoomRepository.upsert(actionEntity)
    }

    @MustBeCalledInTransactionContext
    private fun upsertRoomEntry(roomEntry: RoomEntry): RoomEntry {
        val roomAdded = roomRepository.upsert(roomEntry.toSqlEntity())
        val roomId = roomAdded.id!!

        val usersAdded = upsertUsers(roomId, roomEntry)
        val moderatorsAdded = upsertModerators(roomId, roomEntry)
        val lasActionAdded = roomEntry.lastAction?.let { upsertLastAction(roomId, it) }

        return RoomEntry.fromEntities(roomAdded, usersAdded, moderatorsAdded, lasActionAdded)
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
    override fun getRoomCount(userId: Long): Long {
        return userToRoomRepository.countUserRooms(userId)
    }

    @MustBeCalledInTransactionContext
    override fun getRoom(roomId: Long): RoomEntry {
        val roomEntity = roomRepository.getByIdOrNull(roomId)
            ?: throw RoomEntryNotFoundException(searchCriteria = "id=$roomId")

        val usersEntities = userToRoomRepository.getUsersByRoomId(roomId)
        val moderatorsEntities = moderatorToRoomRepository.getUsersByRoomId(roomId)
        val actionToRoomSqlEntity = actionToRoomRepository.getByRoomId(roomId)
            ?: throw LastActionNotFoundException(searchCriteria = "id=$roomId")

        return RoomEntry.fromEntities(
            roomSqlEntity = roomEntity,
            usersToRoomSqlEntities = usersEntities,
            moderatorsToRoomSqlEntities = moderatorsEntities,
            actionToRoomSqlEntity = actionToRoomSqlEntity
        )
    }

    @MustBeCalledInTransactionContext
    override fun findRoom(userId: Long, collocutorId: Long): RoomEntry? {
        val roomEntity = roomRepository.findRoom(userId, collocutorId) ?: return null

        val roomId = roomEntity.id!!
        val usersEntities = userToRoomRepository.getUsersByRoomId(roomId)
        val moderatorsEntities = moderatorToRoomRepository.getUsersByRoomId(roomId)
        val actionToRoomSqlEntity = actionToRoomRepository.getByRoomId(roomId)
            ?: throw LastActionNotFoundException(searchCriteria = "id=$roomId")

        return RoomEntry.fromEntities(
            roomSqlEntity = roomEntity,
            usersToRoomSqlEntities = usersEntities,
            moderatorsToRoomSqlEntities = moderatorsEntities,
            actionToRoomSqlEntity = actionToRoomSqlEntity
        )
    }

    @MustBeCalledInTransactionContext
    override fun getRoomsPaging(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry> {
        val roomEntities = roomRepository.getRoomsPaging(userId = userId, page = page, pageSize = pageSize)
        return roomEntities.map { entity ->
            val roomId = entity.id!!

            val usersEntities = userToRoomRepository.getUsersByRoomId(roomId)
            val moderatorsEntities = moderatorToRoomRepository.getUsersByRoomId(roomId)
            val lastActionsEntity = actionToRoomRepository.getByRoomId(roomId)
                ?: throw LastActionNotFoundException(searchCriteria = "id=$roomId")

            RoomEntry.fromEntities(
                roomSqlEntity = entity,
                usersToRoomSqlEntities = usersEntities,
                moderatorsToRoomSqlEntities = moderatorsEntities,
                actionToRoomSqlEntity = lastActionsEntity,
            )
        }
    }

    @MustBeCalledInTransactionContext
    override fun deleteRoom(entry: RoomEntry): RoomEntry {
        val roomId = requireNotNull(entry.id) { "room.id must not be null" }
        if (!roomRepository.hasEntityWithId(roomId)) {
            throw RoomEntryNotFoundException(searchCriteria = "id=$roomId")
        }
        return upsertRoomEntry(entry)
    }


}