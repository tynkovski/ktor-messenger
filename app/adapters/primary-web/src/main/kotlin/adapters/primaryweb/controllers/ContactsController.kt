package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.models.requests.contact.ContactRequest
import adapters.primaryweb.models.responses.SimpleMessageResponse
import adapters.primaryweb.models.responses.user.ContactsResponse
import adapters.primaryweb.util.receiveValidated
import core.usecase.AddToContactsUsecase
import core.usecase.GetContactsUsecase
import core.usecase.RemoveFromContactsUsecase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class ContactsController(
    private val getContactsUsecase: GetContactsUsecase,
    private val addToContactsUsecase: AddToContactsUsecase,
    private val removeFromContactsUsecase: RemoveFromContactsUsecase
) : UserPrincipalController {

    suspend fun getContacts(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val contacts = getContactsUsecase.getContacts(userId)
        call.respond(status = HttpStatusCode.OK, message = ContactsResponse(contacts.toList()))
    }

    suspend fun addContact(call: ApplicationCall) {
        val request = call.receiveValidated<ContactRequest>()
        val userId = findUser(call).id!!
        addToContactsUsecase.addToContacts(userId, request.userId)
        call.respond(status = HttpStatusCode.OK, message = SimpleMessageResponse("ok"))
    }

    suspend fun removeContact(call: ApplicationCall) {
        val request = call.receiveValidated<ContactRequest>()
        val userId = findUser(call).id!!
        removeFromContactsUsecase.removeFromContacts(userId, request.userId)
        call.respond(status = HttpStatusCode.OK, message = SimpleMessageResponse("ok"))
    }
}