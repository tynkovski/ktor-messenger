package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.auth.RestLogoutRequest
import adapters.primaryweb.models.requests.auth.RestRefreshRequest
import adapters.primaryweb.models.requests.user.RestSaveUserRequest
import adapters.primaryweb.models.requests.auth.RestSignInRequest
import adapters.primaryweb.models.responses.RestAccessResponse
import adapters.primaryweb.models.responses.RestTokenResponse
import adapters.primaryweb.util.receiveValidated
import core.models.UserEntry
import core.security.hashing.SaltedHash
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal class AuthController(
    private val saltedHashUsecase: GenerateSaltedHashUsecase,
    private val addUserUsecase: AddUserUsecase,
    private val verifyPasswordUsecase: VerifyPasswordUsecase,
    private val getUserByLoginUsecase: GetUserByLoginUsecase,
    private val createAndSaveTokensUsecase: CreateAndSaveTokensUsecase,
    private val verifyAccessTokenUsecase: VerifyAccessTokenUsecase,
    private val getUserForKeysUsecase: FindUserForKeysUsecase,
    private val createAccessTokenUsecase: CreateAccessTokenUsecase,
    private val deleteRefreshTokenUsecase: DeleteRefreshTokenUsecase,
    private val verifyRefreshTokenUsecase: VerifyRefreshTokenUsecase,
    private val createTokensUsecase: CreateAndSaveTokensUsecase,
) {
    suspend fun signIn(call: ApplicationCall) {
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

    suspend fun signUp(call: ApplicationCall) {
        val request = call.receiveValidated<RestSaveUserRequest>()
        val generated = saltedHashUsecase.generate(request.password, 32)
        val userToSave = UserEntry(
            id = null,
            name = request.name,
            login = request.login,
            password = generated.hash,
            salt = generated.salt,
        )
        val user = addUserUsecase.addUser(userToSave)
        val token = createTokensUsecase.createAndSaveTokens(user)
        call.respond(status = HttpStatusCode.OK, message = token.toResponse())
    }

    suspend fun refreshToken(call: ApplicationCall) {
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

    suspend fun logout(call: ApplicationCall) {
        val request = call.receiveValidated<RestLogoutRequest>()

        val decodedRefreshToken = runCatching {
            verifyRefreshTokenUsecase.verifyRefreshToken(request.refreshToken)
        }.getOrElse { throw Exception("invalid token") }

        val refreshKey = decodedRefreshToken.getClaim("key").asString()

        deleteRefreshTokenUsecase.deleteRefreshToken(refreshKey)

        call.respond(HttpStatusCode.OK, message = "Logged out successfully.")
    }
}