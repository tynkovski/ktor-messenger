package adapters.primaryweb.routes

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.RestSaveUserRequest
import adapters.primaryweb.util.receiveValidated
import core.models.UserEntry
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.userRoute() {
    route("/users") {
        registerUser()
        authenticate {
            getUser()
            updateUser()
            deleteUser()
        }
    }
}

private fun Route.registerUser() {
    val saltedHashUsecase by inject<GenerateSaltedHashUsecase>()
    val userUsecase by inject<AddUserUsecase>()
    post {
        val request = call.receiveValidated<RestSaveUserRequest>()
        val generated = saltedHashUsecase.generate(request.password, 32)
        val userToSave = UserEntry(
            id = null,
            name = request.name,
            login = request.login,
            password = generated.hash,
            salt = generated.salt,
        )
        val user = userUsecase.addUser(userToSave)
        val response = user.toResponse()
        call.respond(status = HttpStatusCode.OK, message = response)
    }
}

private fun Route.getUser() {
    val userUsecase by inject<GetUserUsecase>()
    get("{id}") {

    }
}

private fun Route.updateUser() {
    val userUsecase by inject<UpdateUserUsecase>()
    put("{id}") {

    }
}

private fun Route.deleteUser() {
    val userUsecase by inject<DeleteUserUsecase>()
    delete("{id}") {

    }
}


