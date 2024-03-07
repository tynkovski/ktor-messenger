package core.usecase

import core.models.RoomEntry

fun interface AddRoomUsecase {
    suspend fun addRoom(entry: RoomEntry): RoomEntry
}

fun interface GetRoomUsecase {
    suspend fun getRoom(id: Long): RoomEntry
}

fun interface GetRoomCountUsecase {
    suspend fun getRoomCount(userId: Long): Long
}

fun interface GetRoomsPagingUsecase {
    suspend fun getRooms(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry>
}

fun interface DeleteRoomUsecase {
    suspend fun deleteRoom(id: Long)
}

fun interface UpdateRoomUsecase {
    suspend fun updateRoom(entry: RoomEntry): RoomEntry
}

fun interface RenameRoomUsecase {
    suspend fun renameRoom(id: Long, name: String?): RoomEntry
}

fun interface AddUserToRoomUsecase {
    suspend fun addUser(userId: Long, entry: RoomEntry): RoomEntry
}

fun interface RemoveUserFromRoomUsecase {
    suspend fun removeUser(userId: Long, entry: RoomEntry): RoomEntry
}

fun interface AddModeratorToRoomUsecase {
    suspend fun addModerator(userId: Long, entry: RoomEntry): RoomEntry
}

fun interface RemoveModeratorFromRoomUsecase {
    suspend fun removeModerator(userId: Long, entry: RoomEntry): RoomEntry
}
