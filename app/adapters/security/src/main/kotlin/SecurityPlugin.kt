import service.JWTPrincipalExtended
import core.outport.TokenConfig
import core.usecase.FindUserForAccessKeyUsecase
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject
import usecase.AccessTokenVerifierUsecase

fun Application.setupSecurity() {
    val accessTokenVerifier by inject<AccessTokenVerifierUsecase>()
    val tokenConfig by inject<TokenConfig>()
    val findUserForAccessKeyUsecase by inject<FindUserForAccessKeyUsecase>()
    install(Authentication) {
        jwt {
            realm = "Messenger" // todo get from config
            verifier(accessTokenVerifier.accessTokenVerifier())
            validate { credential ->
                val payload = credential.payload
                val accessKey = payload.getClaim("key").asString()
                val user = findUserForAccessKeyUsecase.findUserForAccessKey(accessKey)

                if (user != null && payload.audience.contains(tokenConfig.audience)) {
                    JWTPrincipalExtended(payload, user)
                } else {
                    null
                }
            }
        }
    }
}