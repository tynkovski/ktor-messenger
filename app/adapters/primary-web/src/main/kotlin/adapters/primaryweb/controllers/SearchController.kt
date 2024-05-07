package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.contact.FindUserRequest
import adapters.primaryweb.util.receiveValidated
import core.usecase.GetUserByLoginUsecase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class SearchController(
    private val getUserUsecase: GetUserByLoginUsecase,
) : UserPrincipalController {
    suspend fun getUserByLogin(call: ApplicationCall) {
        val request = call.receiveValidated<FindUserRequest>()
        val user = getUserUsecase.getUser(request.login)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }
}