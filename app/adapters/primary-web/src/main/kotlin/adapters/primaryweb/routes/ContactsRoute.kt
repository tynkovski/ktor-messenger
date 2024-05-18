package adapters.primaryweb.routes

import adapters.primaryweb.controllers.ContactsController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.contactsRoute() {
    val controller by inject<ContactsController>()
    route("/contact") {
        authenticate {
            get { controller.getContacts(call) }
            post { controller.addContact(call) }
            delete { controller.removeContact(call) }
        }
    }
}


