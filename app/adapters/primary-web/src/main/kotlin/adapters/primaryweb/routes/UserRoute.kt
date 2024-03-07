package adapters.primaryweb.routes

import adapters.primaryweb.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.userRoute() {
    val controller by inject<UserController>()
    route("/user") {
        post { controller.addUser(call) }
        get("{id}") { controller.getUserById(call) }
        authenticate {
            get { controller.getUser(call) }
            put { controller.updateUser(call) }
            delete { controller.deleteUser(call) }
        }
    }
}


