package adapters.primaryweb

import adapters.primaryweb.configs.configureRoutes
import adapters.primaryweb.configs.configurePlugins
import adapters.primaryweb.configs.configureSecurity
import io.ktor.server.application.*

fun Application.webBootstrap() {
    configurePlugins()
    configureSecurity()
    configureRoutes()
}
