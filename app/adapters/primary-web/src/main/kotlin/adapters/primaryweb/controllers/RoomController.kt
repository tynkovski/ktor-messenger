package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.room.*
import adapters.primaryweb.util.longParameter
import adapters.primaryweb.util.receiveValidated
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json

sealed class RoomControllerEvent(override val name: String) : BaseControllerEvent {
    companion object {
        inline fun <reified R : Any> fromRequest(request: String): RoomControllerEvent {
            return when (val json = Json.decodeFromString<R>(request)) {
                is CreateRoomRequest -> CreateRoom(json)
                is RenameRoomRequest -> RenameRoom(json)
                is DeleteRoomRequest -> DeleteRoom(json)
                is JoinToRoomRequest -> JoinedToRoom(json)
                is QuitFromRoomRequest -> QuitFromRoom(json)
                is InviteUserToRoomRequest -> InviteUserToRoom(json)
                is KickUserFromRoomRequest -> KickUserFromRoom(json)
                is MakeModeratorToRoomRequest -> MakeUserModeratorToRoom(json)
                else -> Unknown
            }
        }

        const val CREATE_ROOM = "create_room"
        const val RENAME_ROOM = "rename_room"
        const val DELETE_ROOM = "delete_room"
        const val INVITE_USER_TO_ROOM = "invite_user_to_room"
        const val KICK_USER_FROM_ROOM = "kick_user_from_room"
        const val JOINED_TO_ROOM = "joined_to_room"
        const val QUIT_FROM_ROOM = "quit_from_room"
        const val MAKE_MODERATOR = "make_moderator"
        const val UNKNOWN = "unknown"
    }

    data class CreateRoom(
        val request: CreateRoomRequest
    ) : RoomControllerEvent(CREATE_ROOM)

    data class RenameRoom(
        val request: RenameRoomRequest
    ) : RoomControllerEvent(RENAME_ROOM)

    data class DeleteRoom(
        val request: DeleteRoomRequest
    ) : RoomControllerEvent(DELETE_ROOM)

    data class JoinedToRoom(
        val request: JoinToRoomRequest
    ) : RoomControllerEvent(JOINED_TO_ROOM)

    data class QuitFromRoom(
        val request: QuitFromRoomRequest
    ) : RoomControllerEvent(QUIT_FROM_ROOM)

    data class InviteUserToRoom(
        val request: InviteUserToRoomRequest
    ) : RoomControllerEvent(INVITE_USER_TO_ROOM)

    data class KickUserFromRoom(
        val request: KickUserFromRoomRequest
    ) : RoomControllerEvent(KICK_USER_FROM_ROOM)

    data class MakeUserModeratorToRoom(
        val request: MakeModeratorToRoomRequest
    ) : RoomControllerEvent(MAKE_MODERATOR)

    data object Unknown : RoomControllerEvent(UNKNOWN)
}

internal class RoomController(
    private val createRoomUsecase: CreateRoomUsecase,
    private val getRoomUsecase: GetRoomUsecase,
    private val findRoomUsecase: FindRoomUsecase,
    private val deleteRoomUsecase: DeleteRoomUsecase,
    private val renameRoomUsecase: RenameRoomUsecase,
    private val joinToRoomUsecase: JoinToRoomUsecase,
    private val quitFromRoomUsecase: QuitFromRoomUsecase,
    private val inviteUserToRoomUsecase: InviteUserToRoomUsecase,
    private val kickUserFromRoomUsecase: KickUserFromRoomUsecase,
    private val makeModeratorInRoomUsecase: MakeModeratorInRoomUsecase,
    private val getRoomsPagedUsecase: GetRoomsPagingUsecase,
    private val getRoomsCountUsecase: GetRoomCountUsecase,
) : BaseWebsocketsController<RoomControllerEvent>() {

    suspend fun getRoom(call: ApplicationCall) {
        val roomId = call.longParameter("id")
        val userId = findUser(call).id!!
        val room = getRoomUsecase.getRoom(roomId)
        if (!room.users.contains(userId)) {
            throw Exception("User with id $userId does not participate in conversation")
        }
        call.respond(status = HttpStatusCode.OK, message = room.toResponse())
    }

    suspend fun findRoom(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val collocutorId = call.longParameter("id")
        val room = findRoomUsecase.findRoom(userId, collocutorId)
        if (room == null) {
            val createdRoom = createRoomUsecase.createRoom(
                applicantId = userId,
                name = null,
                image = null,
                users = setOf(userId, collocutorId),
                moderators = emptySet()
            )
            call.respond(status = HttpStatusCode.OK, message = createdRoom.toResponse())
        } else {
            call.respond(status = HttpStatusCode.OK, message = room.toResponse())
        }
    }

    suspend fun createRoom(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val request = call.receiveValidated<CreateRoomRequest>()
        val room = createRoomUsecase.createRoom(
            applicantId = userId,
            name = request.name,
            image = request.image,
            users = request.users.toSet() + userId,
            moderators = emptySet()
        )
        call.respond(status = HttpStatusCode.OK, message = room.toResponse())
    }

    suspend fun getRoomsPaged(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val page = call.parameters["page"]?.toLong() ?: 0
        val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
        val count = getRoomsCountUsecase.getRoomCount(userId)
        val rooms = getRoomsPagedUsecase.getRooms(userId, page, pageSize)
        call.respond(status = HttpStatusCode.OK, message = rooms.toResponse(count))
    }

    override suspend fun processEvent(applicantId: Long, event: RoomControllerEvent) {
        when (event) {
            is RoomControllerEvent.CreateRoom -> with(event.request) {
                val usersSet = users.toSet()
                val room = createRoomUsecase.createRoom(
                    applicantId = applicantId,
                    name = name,
                    image = image,
                    users = usersSet,
                    moderators = usersSet
                )
                notifyUsersIfConnected(usersSet, event.name, room.toResponse())
            }

            is RoomControllerEvent.RenameRoom -> with(event.request) {
                val room = renameRoomUsecase.renameRoom(applicantId, roomId, name)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            is RoomControllerEvent.DeleteRoom -> with(event.request) {
                val usersSet = getRoomUsecase.getRoom(roomId).users.toSet()
                val room = deleteRoomUsecase.deleteRoom(roomId)
                notifyUsersIfConnected(usersSet, event.name, room.toResponse())
            }

            is RoomControllerEvent.JoinedToRoom -> with(event.request) {
                val room = joinToRoomUsecase.joinUser(applicantId, roomId)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            is RoomControllerEvent.QuitFromRoom -> with(event.request) {
                val room = quitFromRoomUsecase.quitUser(applicantId, roomId)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            is RoomControllerEvent.InviteUserToRoom -> with(event.request) {
                val room = inviteUserToRoomUsecase.inviteUser(applicantId, userId, roomId)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            is RoomControllerEvent.KickUserFromRoom -> with(event.request) {
                val room = kickUserFromRoomUsecase.kickUser(applicantId, userId, roomId)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            is RoomControllerEvent.MakeUserModeratorToRoom -> with(event.request) {
                val room = makeModeratorInRoomUsecase.makeModerator(applicantId, userId, roomId)
                notifyUsersIfConnected(room.users, event.name, room.toResponse())
            }

            RoomControllerEvent.Unknown -> {
                notifyUsersIfConnected(setOf(applicantId), event.name, Unit)
            }
        }
    }

    override suspend fun processText(event: String, json: String): RoomControllerEvent {
        return when (event) {
            RoomControllerEvent.CREATE_ROOM ->
                RoomControllerEvent.fromRequest<CreateRoomRequest>(json)

            RoomControllerEvent.RENAME_ROOM ->
                RoomControllerEvent.fromRequest<RenameRoomRequest>(json)

            RoomControllerEvent.DELETE_ROOM ->
                RoomControllerEvent.fromRequest<DeleteRoomRequest>(json)

            RoomControllerEvent.JOINED_TO_ROOM ->
                RoomControllerEvent.fromRequest<JoinToRoomRequest>(json)

            RoomControllerEvent.QUIT_FROM_ROOM ->
                RoomControllerEvent.fromRequest<QuitFromRoomRequest>(json)

            RoomControllerEvent.INVITE_USER_TO_ROOM ->
                RoomControllerEvent.fromRequest<InviteUserToRoomRequest>(json)

            RoomControllerEvent.KICK_USER_FROM_ROOM ->
                RoomControllerEvent.fromRequest<KickUserFromRoomRequest>(json)

            RoomControllerEvent.MAKE_MODERATOR ->
                RoomControllerEvent.fromRequest<MakeModeratorToRoomRequest>(json)

            else -> RoomControllerEvent.Unknown
        }
    }
}