package adapters.primaryweb.routes

import com.github.michaelbull.logging.InlineLogger
import adapters.primaryweb.controllers.RoomController
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.roomRoute() {
    val roomController by inject<RoomController>()

    webSocket("/room") {
        try {
            roomController.connect(this)
            roomController.listen(this)
        } catch (e: Exception) {
            logger.error(e) { "roomRoute() ${e.printStackTrace()}" }
        } finally {
            roomController.disconnect(this)
        }
    }
}

internal fun Route.chatRoute() {
    webSocket("/chat") {

    }
}
