package adapters.primaryweb.routes

import adapters.primaryweb.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.userRoute() {
    val controller by inject<UserController>()
    route("/user") {
        post { controller.createUser(call) }
        authenticate {
            get { controller.getUser(call) }
            get("{id}") { controller.getUserById(call) }

            put("/edit") { controller.updateUser(call) }
            put("/image") { controller.editUserImage(call) }
            put("/name") { controller.editUserName(call) }

            delete { controller.deleteUser(call) }
        }
    }
}


