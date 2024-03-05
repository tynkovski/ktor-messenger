package adapters.primaryweb.configs

import adapters.primaryweb.routes.chatRoute
import adapters.primaryweb.routes.roomRoute
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

internal fun Application.configureWebSockets() {
    routing {
        authenticate {
            roomRoute()
            chatRoute()
        }
    }
}


