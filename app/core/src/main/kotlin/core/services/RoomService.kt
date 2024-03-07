package core.services

import core.models.RoomEntry
import core.outport.*
import core.usecase.*

internal class AddRoomService(
    private val addRoomPort: AddRoomPort,
    private val txPort: PersistTransactionPort,
) : AddRoomUsecase {
    override suspend fun addRoom(entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            addRoomPort.addRoom(entry)
        }
}

internal class GetRoomService(
    private val getRoomPort: GetRoomPort,
    private val txPort: PersistTransactionPort,
) : GetRoomUsecase {
    override suspend fun getRoom(id: Long): RoomEntry =
        txPort.withNewTransaction {
            getRoomPort.getRoom(id)
        }
}

internal class GetRoomsPagingService(
    private val getRoomsPort: GetRoomsPagingPort,
    private val txPort: PersistTransactionPort,
) : GetRoomsPagingUsecase {
    override suspend fun getRooms(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry> =
        txPort.withNewTransaction {
            getRoomsPort.getRoomsPaging(userId = userId, page = page, pageSize = pageSize)
        }
}

internal class DeleteRoomService(
    private val deleteRoomPort: DeleteRoomPort,
    private val txPort: PersistTransactionPort,
) : DeleteRoomUsecase {
    override suspend fun deleteRoom(id: Long) =
        txPort.withNewTransaction {
            deleteRoomPort.deleteRoom(id)
        }
}

internal class UpdateRoomService(
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : UpdateRoomUsecase {
    override suspend fun updateRoom(entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            updateRoomPort.updateRoom(entry)
        }
}

internal class RenameRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : RenameRoomUsecase {
    override suspend fun renameRoom(id: Long, name: String?): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(id).copy(name = name)
            updateRoomPort.updateRoom(room)
        }
}

internal class AddUserToRoomService(
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : AddUserToRoomUsecase {
    override suspend fun addUser(userId: Long, entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            val users = entry.users + userId
            val newRoom = entry.copy(users = users)
            updateRoomPort.updateRoom(newRoom)
        }
}

internal class RemoveUserFromRoomService(
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : RemoveUserFromRoomUsecase {
    override suspend fun removeUser(userId: Long, entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            val users = entry.users - userId
            val newRoom = entry.copy(users = users)
            updateRoomPort.updateRoom(newRoom)
        }
}

internal class AddModeratorToRoomService(
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : AddModeratorToRoomUsecase {
    override suspend fun addModerator(userId: Long, entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            val moderators = entry.moderators + userId
            val newRoom = entry.copy(moderators = moderators)
            updateRoomPort.updateRoom(newRoom)
        }
}

internal class RemoveModeratorFromRoomService(
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : RemoveModeratorFromRoomUsecase {
    override suspend fun removeModerator(userId: Long, entry: RoomEntry): RoomEntry =
        txPort.withNewTransaction {
            val moderators = entry.moderators - userId
            val newRoom = entry.copy(moderators = moderators)
            updateRoomPort.updateRoom(newRoom)
        }
}
