package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.util.longParameter
import core.usecase.AddToContactsUsecase
import core.usecase.GetContactsUsecase
import core.usecase.GetUsersUsecase
import core.usecase.RemoveFromContactsUsecase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class ContactsController(
    private val getContactsUsecase: GetContactsUsecase,
    private val getUsersUsecase: GetUsersUsecase,
    private val addToContactsUsecase: AddToContactsUsecase,
    private val removeFromContactsUsecase: RemoveFromContactsUsecase
) : UserPrincipalController {

    suspend fun getContacts(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val contacts = getContactsUsecase.getContacts(userId)
        val users = getUsersUsecase.getUsers(contacts.toList())
        call.respond(status = HttpStatusCode.OK, message = users.toResponse())
    }

    suspend fun addContact(call: ApplicationCall) {
        val userId = call.longParameter("id")
        addToContactsUsecase.addToContacts(userId, userId)
        val contacts = getContactsUsecase.getContacts(userId)
        val users = getUsersUsecase.getUsers(contacts.toList())
        call.respond(status = HttpStatusCode.OK, message = users.toResponse())
    }

    suspend fun removeContact(call: ApplicationCall) {
        val userId = call.longParameter("id")
        removeFromContactsUsecase.removeFromContacts(userId, userId)
        val contacts = getContactsUsecase.getContacts(userId)
        val users = getUsersUsecase.getUsers(contacts.toList())
        call.respond(status = HttpStatusCode.OK, message = users.toResponse())
    }
}