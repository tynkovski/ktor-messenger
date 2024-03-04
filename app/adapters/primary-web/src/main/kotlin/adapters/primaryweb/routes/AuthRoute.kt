package adapters.primaryweb.routes

import adapters.primaryweb.models.requests.RestLogoutRequest
import adapters.primaryweb.models.requests.RestRefreshRequest
import adapters.primaryweb.models.requests.RestSignInRequest
import adapters.primaryweb.models.responses.RestAccessResponse
import adapters.primaryweb.models.responses.RestTokenResponse
import adapters.primaryweb.util.receiveValidated
import core.security.hashing.SaltedHash
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

internal fun Routing.authRoute() {
    route("/auth") {
        signUp()
        signIn()
        refreshToken()
        authenticate {
            logout()
        }
    }
}

private fun Route.signUp() {
    post("/signUp") {
        // todo create redirect to [post] /user
    }
}

private fun Route.signIn() {
    val verifyPasswordUsecase by inject<VerifyPasswordUsecase>()
    val getUserByLoginUsecase by inject<GetUserByLoginUsecase>()
    val createAndSaveTokensUsecase by inject<CreateAndSaveTokensUsecase>()

    post("/signIn") {
        val request = call.receiveValidated<RestSignInRequest>()

        val user = getUserByLoginUsecase.getUser(request.login)

        val isValidPassword = verifyPasswordUsecase.verify(
            password = request.password,
            saltedHash = SaltedHash(user.password, user.salt)
        )

        if (!isValidPassword) throw Exception("Incorrect password") // todo create exceptions

        val token = createAndSaveTokensUsecase.createAndSaveTokens(user)

        call.respond(HttpStatusCode.OK, RestTokenResponse(token.accessToken, token.refreshToken))
    }
}

private fun Route.refreshToken() {
    val verifyRefreshTokenUsecase by inject<VerifyRefreshTokenUsecase>()
    val verifyAccessTokenUsecase by inject<VerifyAccessTokenUsecase>()
    val getUserForKeysUsecase by inject<FindUserForKeysUsecase>()
    val createAccessTokenUsecase by inject<CreateAccessTokenUsecase>()

    post("/refreshToken") {
        val request = call.receiveValidated<RestRefreshRequest>()
        val authHeader = call.request.headers["Authorization"]
        if (authHeader == null) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            val accessToken = authHeader.replace("Bearer\\s+".toRegex(), "")
            val refreshToken = request.refreshToken

            val decodedAccessToken = runCatching {
                verifyAccessTokenUsecase.verifyAccessToken(accessToken, true)
            }.getOrElse { throw Exception("Invalid access token.") }

            val decodedRefreshToken = runCatching {
                verifyRefreshTokenUsecase.verifyRefreshToken(refreshToken)
            }.getOrElse { throw Exception("Invalid refresh token.") }

            val accessKey = decodedAccessToken.getClaim("key").asString()
            val refreshKey = decodedRefreshToken.getClaim("key").asString()

            if (accessKey == null || refreshKey == null) {
                throw Exception("Invalid token.")
            }

            val userEntity = getUserForKeysUsecase.findUserForKeys(
                accessKey = accessKey,
                refreshKey = refreshKey
            ) ?: throw Exception("Invalid token.")

            val newAccessToken = createAccessTokenUsecase.createAndSaveAccessToken(
                user = userEntity,
                refreshKey = refreshKey
            )

            call.respond(HttpStatusCode.OK, RestAccessResponse(accessToken = newAccessToken))
        }
    }
}

private fun Route.logout() {
    val verifyRefreshTokenUsecase by inject<VerifyRefreshTokenUsecase>()
    val deleteRefreshTokenUsecase by inject<DeleteRefreshTokenUsecase>()

    post("/logout") {
        val request = call.receiveValidated<RestLogoutRequest>()

        val decodedRefreshToken = runCatching {
            verifyRefreshTokenUsecase.verifyRefreshToken(request.refreshToken)
        }.getOrElse { throw Exception("invalid token") }

        val refreshKey = decodedRefreshToken.getClaim("key").asString()

        deleteRefreshTokenUsecase.deleteRefreshToken(refreshKey)

        call.respond(HttpStatusCode.OK, message = "Logged out successfully.")
    }
}
