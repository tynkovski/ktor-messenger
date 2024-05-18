package adapters.primaryweb.controllers.interfaces

import core.security.token.JWTUserPrincipal
import io.ktor.server.application.*
import io.ktor.server.auth.*

interface UserPrincipalController {
    fun findUser(call: ApplicationCall) = checkNotNull(call.principal<JWTUserPrincipal>()).user
}