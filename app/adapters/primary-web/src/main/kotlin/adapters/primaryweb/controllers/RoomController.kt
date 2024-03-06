package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.WebsocketsCreateRoomRequest
import adapters.primaryweb.util.ifCanDecodeAs
import core.models.RoomEntry
import core.usecase.AddRoomUsecase
import io.ktor.server.websocket.*
import io.ktor.websocket.*

internal class RoomController(
    private val addRoomUsecase: AddRoomUsecase,
) : BaseWebsocketsController() {

    private fun buildRoom(request: WebsocketsCreateRoomRequest): RoomEntry {
        return RoomEntry(
            id = null,
            name = request.name,
            image = request.image,
            users = request.users.toSet(),
            moderators = request.users.toSet(),
        )
    }

    override suspend fun processFrame(session: DefaultWebSocketServerSession, frame: Frame) {
        if (frame is Frame.Text) {
            with(frame.readText()) {
                ifCanDecodeAs<WebsocketsCreateRoomRequest> {
                    val room = buildRoom(this)
                    val entry = addRoomUsecase.addRoom(room)
                    notifyConnectedUsers(room.users.toSet(), entry.toResponse())
                }
            }
        }
    }
}