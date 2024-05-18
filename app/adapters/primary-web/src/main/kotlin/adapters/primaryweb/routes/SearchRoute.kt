package adapters.primaryweb.routes

import adapters.primaryweb.controllers.SearchController
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.searchRoute() {
    val controller by inject<SearchController>()
    route("/search") {
        get("/user") { controller.getUserByLogin(call) }
    }
}
