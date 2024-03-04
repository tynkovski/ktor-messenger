package adapters.primaryweb.routes

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.RestSaveUserRequest
import adapters.primaryweb.util.receiveValidated
import com.github.michaelbull.logging.InlineLogger
import core.models.UserEntry
import core.security.token.JWTPrincipalExtended
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Routing.userRoute() {
    route("/user") {
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
    val addUserUsecase by inject<AddUserUsecase>()
    val createTokensUsecase by inject<CreateAndSaveTokensUsecase>()
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
        val user = addUserUsecase.addUser(userToSave)
        val tokens = createTokensUsecase.createAndSaveTokens(user)
        call.respond(status = HttpStatusCode.OK, message = tokens.toResponse())
    }
}

private fun Route.getUser() {
    // val userUsecase by inject<GetUserUsecase>()
    get {
        val user = checkNotNull(call.principal<JWTPrincipalExtended>()).user
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }
}

private fun Route.updateUser() {
    val userUsecase by inject<UpdateUserUsecase>()
    put {
        // todo create some logic
    }
}

private fun Route.deleteUser() {
    val userUsecase by inject<DeleteUserUsecase>()
    delete {
        val user = checkNotNull(call.principal<JWTPrincipalExtended>()).user
        userUsecase.deleteUser(user.id!!)
        // todo change call body
        call.respond(status = HttpStatusCode.OK, message = "user deleted")
    }
}


