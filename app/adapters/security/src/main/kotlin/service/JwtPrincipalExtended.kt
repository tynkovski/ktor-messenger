package service

import com.auth0.jwt.interfaces.Payload
import core.models.UserEntry
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

class JWTPrincipalExtended(
    payload: Payload,
    val user: UserEntry
) : Principal, JWTPayloadHolder(payload)