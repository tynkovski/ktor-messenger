package core.usecase

import core.models.RoomEntry

fun interface CreateRoomUsecase {
    suspend fun createRoom(
        applicantId: Long,
        name: String?,
        image: String?,
        users: Set<Long>,
        moderators: Set<Long>
    ): RoomEntry
}

fun interface GetRoomUsecase {
    suspend fun getRoom(roomId: Long): RoomEntry
}

fun interface FindRoomUsecase {
    suspend fun findRoom(userId: Long, collocutorId: Long): RoomEntry?
}

fun interface DeleteRoomUsecase {
    suspend fun deleteRoom(roomId: Long): RoomEntry
}

fun interface RenameRoomUsecase {
    suspend fun renameRoom(applicantId: Long, roomId: Long, name: String?): RoomEntry
}

fun interface UpdateRoomUsecase {
    suspend fun updateRoom(roomEntry: RoomEntry): RoomEntry
}

fun interface JoinToRoomUsecase {
    suspend fun joinUser(userId: Long, roomId: Long): RoomEntry
}

fun interface QuitFromRoomUsecase {
    suspend fun quitUser(userId: Long, roomId: Long): RoomEntry
}

fun interface InviteUserToRoomUsecase {
    suspend fun inviteUser(applicantId: Long, userId: Long, roomId: Long): RoomEntry
}

fun interface KickUserFromRoomUsecase {
    suspend fun kickUser(applicantId: Long, userId: Long, roomId: Long): RoomEntry
}

fun interface MakeModeratorInRoomUsecase {
    suspend fun makeModerator(applicantId: Long, userId: Long, roomId: Long): RoomEntry
}

fun interface GetRoomCountUsecase {
    suspend fun getRoomCount(userId: Long): Long
}

fun interface GetRoomsPagingUsecase {
    suspend fun getRooms(userId: Long, page: Long, pageSize: Int): Collection<RoomEntry>
}

fun interface GetRoomUsersUsecase {
    suspend fun getRoomUsers(roomId: Long): Collection<Long>
}