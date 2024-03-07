package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.room.CreateRoomRequest
import adapters.primaryweb.models.requests.room.RenameRoomRequest
import core.models.RoomEntry
import core.usecase.AddRoomUsecase
import core.usecase.GetRoomUsecase
import core.usecase.RenameRoomUsecase
import kotlinx.serialization.json.Json

sealed class RoomControllerEvent : BaseControllerEvent {
    companion object {
        inline fun <reified R : Any> fromRequest(request: String) : RoomControllerEvent {
            return when (val json = Json.decodeFromString<R>(request)) {
                is CreateRoomRequest -> CreateRoom(json)
                is RenameRoomRequest -> RenameRoom(json)
                else -> Unknown
            }
        }
    }

    data class CreateRoom(val roomRequest: CreateRoomRequest) : RoomControllerEvent()
    data class RenameRoom(val renameRoomRequest: RenameRoomRequest) : RoomControllerEvent()
    data object Unknown : RoomControllerEvent()
}

internal class RoomController(
    private val getRoomUsecase: GetRoomUsecase,
    private val addRoomUsecase: AddRoomUsecase,
    private val renameRoomUsecase: RenameRoomUsecase,
) : BaseWebsocketsController<RoomControllerEvent>() {

    private fun buildRoom(request: CreateRoomRequest): RoomEntry {
        return RoomEntry(
            id = null,
            name = request.name,
            image = request.image,
            users = request.users.toSet(),
            moderators = request.users.toSet(),
        )
    }

    override suspend fun processEvent(userId: Long, event: RoomControllerEvent) {
        when (event) {
            is RoomControllerEvent.CreateRoom -> with(event.roomRequest) {
                val room = addRoomUsecase.addRoom(buildRoom(this))
                notifyUsersIfConnected(room.users.toSet(), room.toResponse())
            }

            is RoomControllerEvent.RenameRoom -> with(event.renameRoomRequest) {
                val room = renameRoomUsecase.renameRoom(id, name)
                notifyUsersIfConnected(room.users.toSet(), room.toResponse())
            }

            RoomControllerEvent.Unknown -> Unit
        }
    }

    override suspend fun processText(event: String, json: String): RoomControllerEvent {
        return when (event) {
            "create_room" -> RoomControllerEvent.fromRequest<CreateRoomRequest>(json)
            "rename_room" -> RoomControllerEvent.fromRequest<RenameRoomRequest>(json)
            else -> RoomControllerEvent.Unknown
        }
    }
}