package adapters.primaryweb.routes

import adapters.primaryweb.controllers.RealtimeChatController
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Route.realtimeChatRoute() {
    val controller by inject<RealtimeChatController>()
    authenticate {
        webSocket("/realtimeChat") {
            try {
                controller.connect(this)
            } catch (e: Exception) {
                logger.error(e) { "realimeChatRoute() ${e.printStackTrace()}" }
            } finally {
                controller.disconnect(this)
            }
        }
    }
}