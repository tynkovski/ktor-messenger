package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.user.CreateUserRequest
import adapters.primaryweb.models.requests.user.EditUserImageRequest
import adapters.primaryweb.models.requests.user.EditUserNameRequest
import adapters.primaryweb.models.requests.user.UpdateUserRequest
import adapters.primaryweb.util.longParameter
import adapters.primaryweb.util.receiveValidated
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal class UserController(
    private val getUserUsecase: GetUserUsecase,
    private val createUserUsecase: CreateUserUsecase,
    private val editUserUsecase: EditUserUsecase,
    private val editUserNameUsecase: EditUserNameUsecase,
    private val editUserImageUsecase: EditUserImageUsecase,
    private val deleteUserUsecase: DeleteUserUsecase
) : UserPrincipalController {
    suspend fun createUser(call: ApplicationCall) {
        val request = call.receiveValidated<CreateUserRequest>()
        val user = createUserUsecase.createUser(
            name = request.name,
            image = request.image,
            login = request.login,
            password = request.password
        )
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }

    suspend fun getUser(call: ApplicationCall) {
        val user = findUser(call)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }

    suspend fun getUserById(call: ApplicationCall) {
        val id = call.longParameter("id")
        val user = getUserUsecase.getUser(id)
        call.respond(status = HttpStatusCode.OK, message = user.toResponse())
    }

    suspend fun updateUser(call: ApplicationCall) {
        val request = call.receiveValidated<UpdateUserRequest>()
        val userId = findUser(call).id!!
        val newUser = editUserUsecase.editUser(userId, request.name, request.image)
        call.respond(status = HttpStatusCode.OK, message = newUser.toResponse())
    }

    suspend fun editUserName(call: ApplicationCall) {
        val request = call.receiveValidated<EditUserNameRequest>()
        val userId = findUser(call).id!!
        val newUser = editUserNameUsecase.editUserName(userId, request.name)
        call.respond(status = HttpStatusCode.OK, message = newUser.toResponse())
    }

    suspend fun editUserImage(call: ApplicationCall) {
        val request = call.receiveValidated<EditUserImageRequest>()
        val userId = findUser(call).id!!
        val newUser = editUserImageUsecase.editUserImage(userId, request.image)
        call.respond(status = HttpStatusCode.OK, message = newUser.toResponse())
    }

    suspend fun deleteUser(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val user = deleteUserUsecase.deleteUser(userId)
        call.respond(status = HttpStatusCode.NoContent, message = user.toResponse())
    }
}