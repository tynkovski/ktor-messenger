package core.outport

import core.models.RoomEntry

interface AddRoomPort {
    fun addRoom(entry: RoomEntry): RoomEntry
}

interface GetRoomPort {
    fun getRoom(id: Long): RoomEntry
}

interface GetRoomsPagingPort {
    fun getRoomsPaging(userId: Long, page: Int, pageSize: Int): Collection<RoomEntry>
}

interface DeleteRoomPort {
    fun deleteRoom(id: Long)
}

interface UpdateRoomPort {
    fun updateRoom(entry: RoomEntry): RoomEntry
}