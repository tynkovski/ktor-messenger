package adapters.primaryweb.routes

import adapters.primaryweb.controllers.AuthController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.authRoute() {
    val controller by inject<AuthController>()
    route("/auth") {
        post("/signUp") { controller.signUp(call) }
        post("/signIn") { controller.signIn(call) }
        post("/refreshToken") { controller.refreshToken(call) }
        authenticate {
            post("/logout") { controller.logout(call) }
        }
    }
}
