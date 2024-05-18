package adapters.primaryweb.routes

import adapters.primaryweb.controllers.MessageController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.chatRoute() {
    val messageController by inject<MessageController>()
    authenticate {
        route("/message") {
            get("{id}") { messageController.getMessage(call) }

            route("/unread") {
                get("/{id}") { messageController.getUnreadCont(call) }
            }

            route("/paged") {
                get("{roomId}") { messageController.getMessagesPaged(call) }
            }

            webSocket {
                try {
                    messageController.connect(this)
                } catch (e: Exception) {
                    logger.error(e) { "chatRoute() ${e.printStackTrace()}" }
                } finally {
                    messageController.disconnect(this)
                }
            }
        }
    }
}