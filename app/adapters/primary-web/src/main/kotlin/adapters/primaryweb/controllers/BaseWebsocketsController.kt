package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.models.WebSocketMember
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

abstract class BaseWebsocketsController : UserPrincipalController {
    protected val logger = InlineLogger()
    protected val members = ConcurrentHashMap<Long, WebSocketMember>()

    protected suspend inline fun <reified R : Any> notifyConnectedUsers(
        users: Set<Long>,
        response: @Serializable R
    ) {
        // todo notify each user in coroutine
        for (user in users) {
            if (!members.containsKey(user)) continue
            val json = Json.encodeToString<R>(response)
            val frame = Frame.Text(json)
            members[user]?.session?.outgoing?.send(frame)
        }
    }

    suspend fun connect(session: DefaultWebSocketServerSession) {
        val user = findUser(session.call)
        members[user.id!!] = WebSocketMember(session = session, user = user)
        session.incoming.consumeEach { frame ->
            processFrame(session, frame)
        }
    }

    suspend fun disconnect(session: DefaultWebSocketServerSession) {
        val userId = findUser(session.call).id
        members[userId]?.session?.close()
        if (members.containsKey(userId)) {
            members.remove(userId)
        }
    }

    abstract suspend fun processFrame(session: DefaultWebSocketServerSession, frame: Frame)
}