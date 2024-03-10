package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

interface BaseControllerEvent {
    val name: String
}

abstract class BaseWebsocketsController<BaseControllerEvent> : UserPrincipalController {

    private val members = ConcurrentHashMap<Long, DefaultWebSocketServerSession>()

    protected val logger = InlineLogger()

    protected fun getSession(userId: Long) = members[userId]

    private fun addSession(userId: Long, session: DefaultWebSocketServerSession):DefaultWebSocketServerSession? {
        members[userId] = session
        return getSession(userId)
    }

    private fun removeSession(userId: Long) {
        if (members.containsKey(userId)) {
            members.remove(userId)
        }
    }

    private suspend fun processFrame(userId: Long, frame: Frame) {
        if (frame is Frame.Text) {
            val (event, json) = frame
                .readText()
                .split("#", limit = 2)
                .map(String::trim)
            processEvent(userId, processText(event, json))
        }
    }

    protected suspend inline fun <reified R : Any> notifyUsersIfConnected(
        users: Set<Long>,
        event: String,
        response: @Serializable R
    ) {
        // todo notify each user in coroutine: CoroutineScope(Dispatchers.IO).launch { }
        logger.debug { "notifying users: $users" }
        for (userId in users) {
            getSession(userId)?.let { session ->
                kotlin.runCatching {
                    val json = Json.encodeToString<R>(response)
                    Frame.Text("$event#$json")
                }.onSuccess { frame ->
                    session.outgoing.send(frame)
                }.onFailure { e ->
                    session.outgoing.send(Frame.Text("$event#${e.printStackTrace()}"))
                }
            }
        }
    }

    protected inline fun <reified T : Any> decode(json: String): T {
        return Json.decodeFromString<T>(json)
    }

    suspend fun connect(session: DefaultWebSocketServerSession) {
        val userId = findUser(session.call).id!!
        logger.debug { "connecting user with id $userId" }
        addSession(userId, session)?.incoming?.consumeEach { frame ->
            processFrame(userId, frame)
        }
    }

    suspend fun disconnect(session: DefaultWebSocketServerSession) {
        val userId = findUser(session.call).id!!
        session.close()
        logger.debug { "disconnecting user with id $userId" }
        removeSession(userId)
    }

    protected abstract suspend fun processEvent(applicantId: Long, event: BaseControllerEvent)

    protected abstract suspend fun processText(event: String, json: String): BaseControllerEvent
}