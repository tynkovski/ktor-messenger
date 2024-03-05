package adapters.primaryweb.controllers

import adapters.primaryweb.util.getUser
import com.github.michaelbull.logging.InlineLogger
import core.models.UserEntry
import core.security.token.JWTUserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import models.WebSocketMember
import java.util.concurrent.ConcurrentHashMap

internal class RoomController(
    // private val createRoomUseCase: CreateRoomUsecase
) {
    private val logger = InlineLogger()
    private val members = ConcurrentHashMap<Long, WebSocketMember>()

    fun connect(session: DefaultWebSocketServerSession) {
        val user = session.call.getUser()
        // if (members.containsKey(user.id!!)) throw MemberExistsException(user.id!!)
        members[user.id!!] = WebSocketMember(session = session, user = user)
    }

    suspend fun listen(session: DefaultWebSocketServerSession) {
        session.incoming.consumeEach { frame ->
            if (frame is Frame.Text) {

                session.outgoing.send(Frame.Text(frame.readText()))
                logger.debug { frame.readText() }
            }
        }
    }

    suspend fun disconnect(session: DefaultWebSocketServerSession) {
        val user = session.call.getUser()
        val userId = user.id!!
        members[userId]?.session?.close()

        if (members.containsKey(userId)) {
            members.remove(userId)
        }
    }
}