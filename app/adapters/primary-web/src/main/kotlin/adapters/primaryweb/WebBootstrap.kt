package adapters.primaryweb

import adapters.primaryweb.configs.configureHttp
import adapters.primaryweb.configs.configurePlugins
import adapters.primaryweb.configs.configureSecurity
import adapters.primaryweb.configs.configureWebSockets
import io.ktor.server.application.Application

fun Application.webBootstrap() {
    configurePlugins()
    configureSecurity()
    configureHttp()
    configureWebSockets()
}
