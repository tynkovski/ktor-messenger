package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.room.*
import adapters.primaryweb.models.responses.room.RoomsPagingResponse
import adapters.primaryweb.util.longParameter
import adapters.primaryweb.util.receiveValidated
import core.models.RoomEntry
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal class RoomController(
    private val getUserUsecase: GetUserUsecase,
    private val createRoomUsecase: CreateRoomUsecase,
    private val getRoomUsecase: GetRoomUsecase,
    private val findRoomUsecase: FindRoomUsecase,
    private val getRoomsPagedUsecase: GetRoomsPagingUsecase,
    private val getRoomsCountUsecase: GetRoomCountUsecase,
) : UserPrincipalController {

    private suspend fun getLastActionAuthor(room: RoomEntry): String? {
        val lastAction = room.lastAction ?: return null
        val authorId = lastAction.applicantId
        val user = getUserUsecase.getUser(authorId)
        return user.name ?: user.login
    }

    suspend fun getRoom(call: ApplicationCall) {
        val roomId = call.longParameter("id")
        val user = findUser(call)
        val userId = user.id!!
        val room = getRoomUsecase.getRoom(roomId)
        if (!room.users.contains(userId)) {
            throw Exception("User with id $userId does not participate in conversation")
        }
        val authorName = getLastActionAuthor(room)
        call.respond(status = HttpStatusCode.OK, message = room.toResponse(authorName))
    }

    suspend fun findRoom(call: ApplicationCall) {
        val user = findUser(call)
        val userId = user.id!!
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
            val authorName = user.name ?: user.login
            call.respond(status = HttpStatusCode.OK, message = createdRoom.toResponse(authorName))
        } else {
            val authorName = getLastActionAuthor(room)
            call.respond(status = HttpStatusCode.OK, message = room.toResponse(authorName))
        }
    }

    suspend fun createRoom(call: ApplicationCall) {
        val user = findUser(call)
        val userId = user.id!!
        val request = call.receiveValidated<CreateRoomRequest>()
        val room = createRoomUsecase.createRoom(
            applicantId = userId,
            name = request.name,
            image = request.image,
            users = request.users.toSet() + userId,
            moderators = emptySet()
        )
        val authorName = getLastActionAuthor(room)
        call.respond(status = HttpStatusCode.OK, message = room.toResponse(authorName))
    }

    suspend fun getRoomsPaged(call: ApplicationCall) {
        val user = findUser(call)
        val userId = user.id!!
        val page = call.parameters["page"]?.toLong() ?: 0
        val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
        val count = getRoomsCountUsecase.getRoomCount(userId)
        val rooms = getRoomsPagedUsecase.getRooms(userId, page, pageSize)
        val response = RoomsPagingResponse(
            count = count,
            page = page,
            rooms = rooms.map { room ->
                val authorName = getLastActionAuthor(room)
                room.toResponse(authorName)
            }
        )
        call.respond(status = HttpStatusCode.OK, message = response)
    }
}