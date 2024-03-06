package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.util.getUser
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import adapters.primaryweb.models.WebSocketMember
import adapters.primaryweb.models.requests.WebsocketsCreateRoomRequest
import adapters.primaryweb.models.responses.WebsocketsRoomResponse
import adapters.primaryweb.util.ifCanDecodeAs
import core.models.RoomEntry
import core.usecase.AddRoomUsecase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

internal class RoomController(
    private val addRoomUsecase: AddRoomUsecase,
) {
    private val logger = InlineLogger()
    private val members = ConcurrentHashMap<Long, WebSocketMember>()

    private fun buildRoom(request: WebsocketsCreateRoomRequest): RoomEntry {
        return RoomEntry(
            id = null,
            name = request.name,
            image = request.image,
            users = request.users,
            moderators = request.users,
        )
    }

    private suspend fun notifyConnected(users: List<Long>, room: WebsocketsRoomResponse) {
        for (user in users) {
            if (members.containsKey(user)) continue
            val frame = Frame.Text(Json.encodeToString(room))
            members[user]?.session?.outgoing?.send(frame)
        }
    }

    fun connect(session: DefaultWebSocketServerSession) {
        val user = session.call.getUser()
        // if (members.containsKey(user.id!!)) throw Exception
        members[user.id!!] = WebSocketMember(session = session, user = user)
    }

    suspend fun listen(session: DefaultWebSocketServerSession) {
        session.incoming.consumeEach { frame ->
            if (frame is Frame.Text) {
                with(frame.readText()) {
                    ifCanDecodeAs<WebsocketsCreateRoomRequest> {
                        val room = buildRoom(this)
                        val entry = addRoomUsecase.addRoom(room)
                        notifyConnected(room.users, entry.toResponse())
                    }
                }
            }
        }
    }

    suspend fun disconnect(session: DefaultWebSocketServerSession) {
        val userId = session.call.getUser().id!!

        members[userId]?.session?.close()

        if (members.containsKey(userId)) {
            members.remove(userId)
        }
    }
}