package adapters.primaryweb.routes

import adapters.primaryweb.controllers.RoomController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.roomRoute() {
    val roomController by inject<RoomController>()
    authenticate {
        route("/room") {
            get("/paged") { roomController.getRoomsPaged(call) }
            get("{id}") { roomController.getRoom(call) }
        }
    }

    authenticate {
        webSocket("/room") {
            try {
                roomController.connect(this)
            } catch (e: Exception) {
                logger.error(e) { "roomRoute() ${e.printStackTrace()}" }
            } finally {
                roomController.disconnect(this)
            }
        }
    }
}
