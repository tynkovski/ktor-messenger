package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import core.usecase.GetUserByLoginUsecase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class SearchController(
    private val getUserUsecase: GetUserByLoginUsecase,
) : UserPrincipalController {
    suspend fun getUserByLogin(call: ApplicationCall) {
        val login = call.request.queryParameters["login"]
        val user = getUserUsecase.getUser(login!!)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }
}