package adapters.primaryweb.configs

import com.github.michaelbull.logging.InlineLogger
import core.outport.GetTokenConfigPort
import core.security.token.JWTUserPrincipal
import core.usecase.AccessTokenVerifierUsecase
import core.usecase.FindUserForAccessKeyUsecase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

private val logger = InlineLogger()

internal fun Application.configureSecurity() {
    val accessTokenVerifier by inject<AccessTokenVerifierUsecase>()
    val tokenConfigPort by inject<GetTokenConfigPort>()
    val findUserForAccessKeyUsecase by inject<FindUserForAccessKeyUsecase>()

    install(Authentication) {
        jwt {
            realm = tokenConfigPort.tokenConfig.realm
            verifier(accessTokenVerifier.accessTokenVerifier())
            validate { credential ->
                val payload = credential.payload
                val accessKey = payload.getClaim("key").asString()
                val user = findUserForAccessKeyUsecase.findUserForAccessKey(accessKey)
                if (user != null && payload.audience.contains(tokenConfigPort.tokenConfig.audience)) {
                    JWTUserPrincipal(payload, user)
                } else {
                    null
                }
            }
        }
    }
}