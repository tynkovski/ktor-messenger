package adapters.primaryweb.routes

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.authRoute() {
    route("/auth") {
        loginUser()
        logoutUser()
        refreshToken()
    }
}

private fun Route.loginUser() {
    post("/login") {

    }
}

private fun Route.logoutUser() {
    post("/logout") {

    }
}

private fun Route.refreshToken() {
    post("/refreshToken") {

    }
}
