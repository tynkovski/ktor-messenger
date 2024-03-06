package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.RestEditUserNameRequest
import adapters.primaryweb.models.requests.RestSaveUserRequest
import adapters.primaryweb.models.requests.RestUpdateUserRequest
import adapters.primaryweb.util.receiveValidated
import core.models.UserEntry
import core.security.token.JWTUserPrincipal
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*

internal class UserController(
    private val saltedHashUsecase: GenerateSaltedHashUsecase,
    private val addUserUsecase: AddUserUsecase,
    private val updateUserUsecase: UpdateUserUsecase,
    private val editUserNameUsecase: EditUserNameUsecase,
    private val deleteUserUsecase: DeleteUserUsecase
) : UserPrincipalController {
    private fun buildUser(request: RestSaveUserRequest): UserEntry {
        val generated = saltedHashUsecase.generate(request.password, 32)
        return UserEntry(
            id = null,
            name = request.name,
            login = request.login,
            password = generated.hash,
            salt = generated.salt,
        )
    }

    suspend fun addUser(call: ApplicationCall) {
        val request = call.receiveValidated<RestSaveUserRequest>()
        val userToSave = buildUser(request)
        val user = addUserUsecase.addUser(userToSave)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }

    suspend fun getUser(call: ApplicationCall) {
        val user = findUser(call)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }

    suspend fun updateUser(call: ApplicationCall) {
        val request = call.receiveValidated<RestUpdateUserRequest>()
        val user = findUser(call)
        val newUser = updateUserUsecase.updateUser(user.copy(name = request.name))
        call.respond(status = HttpStatusCode.OK, message = newUser.toResponse())
    }

    suspend fun editUserName(call: ApplicationCall) {
        val request = call.receiveValidated<RestEditUserNameRequest>()
        val user = findUser(call)
        val newUser = editUserNameUsecase.editUserName(user, request.name)
        call.respond(status = HttpStatusCode.OK, message = newUser.toResponse())
    }

    suspend fun deleteUser(call: ApplicationCall) {
        val user = checkNotNull(call.principal<JWTUserPrincipal>()).user
        deleteUserUsecase.deleteUser(user.id!!)
        call.respond(status = HttpStatusCode.NoContent, message = "")
    }
}