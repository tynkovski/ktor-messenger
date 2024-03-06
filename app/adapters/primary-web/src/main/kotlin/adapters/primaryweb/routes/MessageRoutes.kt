package adapters.primaryweb.routes

import adapters.primaryweb.controllers.MessageController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.chatRoute() {
    val messageController by inject<MessageController>()

    webSocket("/chat") {
        try {
            messageController.connect(this)
        } catch (e: Exception) {
            logger.error(e) { "chatRoute() ${e.printStackTrace()}" }
        } finally {
            messageController.disconnect(this)
        }
    }
}