package core.services

import core.models.RoomEntry
import core.outport.*
import core.usecase.*
import java.time.LocalDateTime

private fun buildRoom(
    applicantId: Long,
    name: String?,
    image: String?,
    users: Set<Long>,
    moderators: Set<Long>
): RoomEntry {
    val time = LocalDateTime.now()
    return RoomEntry(
        id = null,
        name = name,
        image = image,
        lastAction = RoomEntry.LastActionEntry(
            applicantId = applicantId,
            actionType = RoomEntry.LastActionEntry.ActionType.USER_CREATE_ROOM,
            description = null,
            actionDateTime = time
        ),
        users = users,
        moderators = moderators,
        createdAt = time,
        deletedAt = null
    )
}

private fun renameRoom(
    applicantId: Long,
    name: String?,
    room: RoomEntry,
) = room.copy(
    name = name,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = applicantId,
        actionType = RoomEntry.LastActionEntry.ActionType.USER_RENAME_ROOM,
        description = name,
        actionDateTime = LocalDateTime.now()
    )
)

private fun clearRoom(
    room: RoomEntry
) = room.copy(
    name = "[deleted room]",
    image = null,
    lastAction = null,
    users = setOf(),
    moderators = setOf(),
    deletedAt = LocalDateTime.now(),
)

// todo add `link` parameter: User joined via link
private fun joinUserToRoom(
    user: Long,
    room: RoomEntry,
) = room.copy(
    users = room.users + user,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = user,
        actionType = RoomEntry.LastActionEntry.ActionType.USER_JOINED,
        description = null,
        actionDateTime = LocalDateTime.now()
    )
)

private fun quitUserFromRoom(
    user: Long,
    room: RoomEntry,
) = room.copy(
    users = room.users - user,
    moderators = room.moderators - user,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = user,
        actionType = RoomEntry.LastActionEntry.ActionType.USER_QUIT,
        description = null,
        actionDateTime = LocalDateTime.now()
    )
)

private fun inviteUserToRoom(
    applicantId: Long,
    user: Long,
    room: RoomEntry,
) = room.copy(
    users = room.users + user,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = applicantId,
        actionType = RoomEntry.LastActionEntry.ActionType.USER_INVITE_USER,
        description = "$user",
        actionDateTime = LocalDateTime.now()
    )
)

private fun kickUserFromRoom(
    applicantId: Long,
    user: Long,
    room: RoomEntry,
) = room.copy(
    users = room.users - user,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = applicantId,
        actionType = RoomEntry.LastActionEntry.ActionType.USER_KICK_USER,
        description = "$user",
        actionDateTime = LocalDateTime.now()
    )
)

private fun addModeratorToRoom(
    applicantId: Long,
    user: Long,
    room: RoomEntry,
) = room.copy(
    moderators = room.moderators + user,
    lastAction = RoomEntry.LastActionEntry(
        applicantId = applicantId,
        actionType = RoomEntry.LastActionEntry.ActionType.MAKE_MODERATOR,
        description = "$user",
        actionDateTime = LocalDateTime.now()
    )
)

internal class CreateRoomService(
    private val addRoomPort: AddRoomPort,
    private val txPort: PersistTransactionPort,
) : CreateRoomUsecase {
    override suspend fun createRoom(
        applicantId: Long,
        name: String?,
        image: String?,
        users: Set<Long>,
        moderators: Set<Long>
    ): RoomEntry =
        txPort.withNewTransaction {
            val room = buildRoom(
                applicantId = applicantId,
                name = name,
                image = image,
                users = users,
                moderators = moderators
            )
            addRoomPort.addRoom(room)
        }
}

internal class GetRoomService(
    private val getRoomPort: GetRoomPort,
    private val txPort: PersistTransactionPort,
) : GetRoomUsecase {
    override suspend fun getRoom(roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            getRoomPort.getRoom(roomId)
        }
}

internal class FindRoomService(
    private val findRoomPort: FindRoomPort,
    private val txPort: PersistTransactionPort,
) : FindRoomUsecase {
    override suspend fun findRoom(userId: Long, collocutorId: Long): RoomEntry? =
        txPort.withNewTransaction {
            findRoomPort.findRoom(userId, collocutorId)
        }
}

internal class GetRoomCountService(
    private val getRoomCountPort: GetRoomCountPort,
    private val txPort: PersistTransactionPort,
) : GetRoomCountUsecase {
    override suspend fun getRoomCount(userId: Long): Long =
        txPort.withNewTransaction {
            getRoomCountPort.getRoomCount(userId)
        }
}

internal class GetRoomUsersService(
    private val getRoomPort: GetRoomPort,
    private val txPort: PersistTransactionPort,
) : GetRoomUsersUsecase {
    override suspend fun getRoomUsers(roomId: Long): Collection<Long> =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            room.users
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
    private val getRoomPort: GetRoomPort,
    private val deleteRoomPort: DeleteRoomPort,
    private val txPort: PersistTransactionPort,
) : DeleteRoomUsecase {
    override suspend fun deleteRoom(roomId: Long) =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val deleted = clearRoom(room)
            deleteRoomPort.deleteRoom(deleted)
        }
}

internal class RenameRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : RenameRoomUsecase {
    override suspend fun renameRoom(applicantId: Long, roomId: Long, name: String?): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val renamedRoom = renameRoom(applicantId, name, room)
            updateRoomPort.updateRoom(renamedRoom)
        }
}

internal class JoinToRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : JoinToRoomUsecase {
    override suspend fun joinUser(userId: Long, roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val roomWithUser = joinUserToRoom(userId, room)
            updateRoomPort.updateRoom(roomWithUser)
        }
}

internal class QuitFromRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : QuitFromRoomUsecase {
    override suspend fun quitUser(userId: Long, roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val roomWithoutUser = quitUserFromRoom(userId, room)
            updateRoomPort.updateRoom(roomWithoutUser)
        }
}

internal class InviteUserToRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : InviteUserToRoomUsecase {
    override suspend fun inviteUser(applicantId: Long, userId: Long, roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val roomWithUser = inviteUserToRoom(applicantId, userId, room)
            updateRoomPort.updateRoom(roomWithUser)
        }
}

internal class KickUserFromRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : KickUserFromRoomUsecase {
    override suspend fun kickUser(applicantId: Long, userId: Long, roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val roomWithoutUser = kickUserFromRoom(applicantId, userId, room)
            updateRoomPort.updateRoom(roomWithoutUser)
        }
}

internal class MakeModeratorInRoomService(
    private val getRoomPort: GetRoomPort,
    private val updateRoomPort: UpdateRoomPort,
    private val txPort: PersistTransactionPort,
) : MakeModeratorInRoomUsecase {
    override suspend fun makeModerator(applicantId: Long, userId: Long, roomId: Long): RoomEntry =
        txPort.withNewTransaction {
            val room = getRoomPort.getRoom(roomId)
            val roomWithModerator = addModeratorToRoom(applicantId, userId, room)
            updateRoomPort.updateRoom(roomWithModerator)
        }
}