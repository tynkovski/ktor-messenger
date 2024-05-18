package adapters.primaryweb.configs

import adapters.primaryweb.routes.*
import adapters.primaryweb.routes.authRoute
import adapters.primaryweb.routes.healthRoute
import adapters.primaryweb.routes.personRoute
import adapters.primaryweb.routes.userRoute
import com.github.michaelbull.logging.InlineLogger
import io.ktor.server.application.*
import io.ktor.server.routing.*

private val logger = InlineLogger()

internal fun Application.configureRoutes() {
    routing {
        trace {
            logger.debug { "routing/trace(): ${it.buildText()}" }
        }
        healthRoute()
        authRoute()
        contactsRoute()
        searchRoute()
        userRoute()
        personRoute()
        roomRoute()
        messageRoute()
        realtimeChatRoute()
    }
}