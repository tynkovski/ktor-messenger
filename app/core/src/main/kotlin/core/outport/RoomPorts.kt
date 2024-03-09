package core.outport

import core.models.RoomEntry

interface AddRoomPort {
    fun addRoom(entry: RoomEntry): RoomEntry
}

interface GetRoomPort {
    fun getRoom(roomId: Long): RoomEntry
}

interface GetRoomCountPort {
    fun getRoomCount(userId: Long): Long
}

interface GetRoomsPagingPort {
    fun getRoomsPaging(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry>
}

interface DeleteRoomPort {
    fun deleteRoom(roomId: Long)
}

interface UpdateRoomPort {
    fun updateRoom(entry: RoomEntry): RoomEntry
}