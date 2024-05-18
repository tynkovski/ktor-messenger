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
import java.util.concurrent.CopyOnWriteArrayList

interface BaseControllerEvent {
    val name: String
}

abstract class BaseWebsocketsController<B: BaseControllerEvent> : UserPrincipalController {

    private val members = ConcurrentHashMap<Long, CopyOnWriteArrayList<DefaultWebSocketServerSession>>()

    protected val logger = InlineLogger()

    protected fun getSessions(userId: Long) = members[userId]

    private fun addSession(
        userId: Long,
        session: DefaultWebSocketServerSession
    ) {
        if (!members.containsKey(userId)) {
            members[userId] = CopyOnWriteArrayList<DefaultWebSocketServerSession>()
        }
        members[userId]?.add(session)
    }

    private fun removeSession(userId: Long, session: DefaultWebSocketServerSession) {
        if (members.containsKey(userId)) {
            members[userId]?.remove(session)
        }
        if (members[userId]?.isEmpty() == true) {
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
        for (userId in users) {
            // todo notify each session in coroutine: CoroutineScope(Dispatchers.IO).launch { }
            getSessions(userId)?.forEach { session ->
                runCatching {
                    val json = Json.encodeToString<R>(response)
                    Frame.Text("$event#$json")
                }.onSuccess { frame ->
                    session.outgoing.send(frame)
                }.onFailure { e ->
                    // todo use response model
                    val errorResponse = e.printStackTrace()
                    session.outgoing.send(Frame.Text("$event#$errorResponse"))
                }
            }
        }
    }

    suspend fun connect(session: DefaultWebSocketServerSession) {
        val userId = findUser(session.call).id!!
        logger.debug { "connected user with id $userId" }
        addSession(userId, session)
        session.incoming.consumeEach { frame ->
            processFrame(userId, frame)
        }
    }

    suspend fun disconnect(session: DefaultWebSocketServerSession) {
        val userId = findUser(session.call).id!!
        session.close()
        removeSession(userId,session)
    }

    protected abstract suspend fun processEvent(applicantId: Long, event: B)

    protected abstract suspend fun processText(event: String, json: String): B
}