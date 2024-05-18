package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.auth.LogoutRequest
import adapters.primaryweb.models.requests.auth.RefreshRequest
import adapters.primaryweb.models.requests.user.CreateUserRequest
import adapters.primaryweb.models.requests.auth.SignInRequest
import adapters.primaryweb.models.responses.auth.AccessResponse
import adapters.primaryweb.models.responses.auth.TokenResponse
import adapters.primaryweb.util.receiveValidated
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal class AuthController(
    private val createUserUsecase: CreateUserUsecase,
    private val getUserByLoginPasswordUsecase: GetUserByLoginPasswordUsecase,
    private val createAndSaveTokensUsecase: CreateAndSaveTokensUsecase,
    private val verifyAccessTokenUsecase: VerifyAccessTokenUsecase,
    private val getUserForKeysUsecase: FindUserForKeysUsecase,
    private val createAccessTokenUsecase: CreateAccessTokenUsecase,
    private val deleteRefreshTokenUsecase: DeleteRefreshTokenUsecase,
    private val verifyRefreshTokenUsecase: VerifyRefreshTokenUsecase,
    private val createTokensUsecase: CreateAndSaveTokensUsecase,
) {
    suspend fun signIn(call: ApplicationCall) {
        val request = call.receiveValidated<SignInRequest>()

        val user = getUserByLoginPasswordUsecase.getUser(
            password = request.password,
            login = request.login
        ) ?: throw Exception("Incorrect password")

        val token = createAndSaveTokensUsecase.createAndSaveTokens(user)

        call.respond(HttpStatusCode.OK, TokenResponse(token.accessToken, token.refreshToken))
    }

    suspend fun signUp(call: ApplicationCall) {
        val request = call.receiveValidated<CreateUserRequest>()

        val user = createUserUsecase.createUser(
            name = request.name,
            image = request.image,
            login = request.login,
            password = request.password
        )

        val token = createTokensUsecase.createAndSaveTokens(user)

        call.respond(status = HttpStatusCode.OK, message = token.toResponse())
    }

    suspend fun refreshToken(call: ApplicationCall) {
        val request = call.receiveValidated<RefreshRequest>()
        val authHeader = call.request.headers["Authorization"]

        if (authHeader == null) {
            call.respond(HttpStatusCode.Unauthorized)
        } else {
            val accessToken = authHeader.replace("Bearer\\s+".toRegex(), "")
            val refreshToken = request.refreshToken

            val decodedAccessToken = runCatching {
                verifyAccessTokenUsecase.verifyAccessToken(accessToken, true)
            }.getOrElse { throw Exception("Invalid access token.") }

            val accessKey = decodedAccessToken.getClaim("key").asString()
                ?: throw Exception("Invalid token.")

            val decodedRefreshToken = runCatching {
                verifyRefreshTokenUsecase.verifyRefreshToken(refreshToken)
            }.getOrElse { throw Exception("Invalid refresh token.") }

            val refreshKey = decodedRefreshToken.getClaim("key").asString()
                ?: throw Exception("Invalid token.")

            val user = getUserForKeysUsecase.findUserForKeys(
                accessKey = accessKey,
                refreshKey = refreshKey
            ) ?: throw Exception("User doesn't exist or deleted.")

            val newAccessToken = createAccessTokenUsecase.createAndSaveAccessToken(
                user = user,
                refreshKey = refreshKey
            )

            call.respond(HttpStatusCode.OK, AccessResponse(accessToken = newAccessToken))
        }
    }

    suspend fun logout(call: ApplicationCall) {
        val request = call.receiveValidated<LogoutRequest>()

        val decodedRefreshToken = runCatching {
            verifyRefreshTokenUsecase.verifyRefreshToken(request.refreshToken)
        }.getOrElse { throw Exception("Invalid refresh token.") }

        val refreshKey = decodedRefreshToken.getClaim("key").asString()

        deleteRefreshTokenUsecase.deleteRefreshToken(refreshKey)

        call.respond(HttpStatusCode.OK, message = "Logged out successfully.")
    }
}