package adapters.primaryweb.routes

import adapters.primaryweb.controllers.MessageController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.messageRoute() {
    val messageController by inject<MessageController>()
    authenticate {
        route("/message") {
            get("{id}") { messageController.getMessage(call) }

            route("/paged") {
                get("{roomId}") { messageController.getMessagesPaged(call) }
            }
        }
    }
}