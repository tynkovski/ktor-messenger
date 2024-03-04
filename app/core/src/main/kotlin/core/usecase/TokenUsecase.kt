package core.usecase

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import core.models.UserEntry
import core.security.token.TokenClaim

fun interface AccessTokenVerifierUsecase {
    fun accessTokenVerifier(): JWTVerifier
}

fun interface GenerateKeyUsecase {
    fun generateKey(): String
}

fun interface GenerateAccessTokenUsecase {
    fun generateAccessToken(key: String, vararg claim: TokenClaim): String
}

fun interface GenerateRefreshTokenUsecase {
    fun generateRefreshToken(key: String, vararg claim: TokenClaim): String
}

fun interface VerifyRefreshTokenUsecase {
    fun verifyRefreshToken(refreshToken: String): DecodedJWT
}

fun interface VerifyAccessTokenUsecase {
    fun verifyAccessToken(accessToken: String, ignoreExpiry: Boolean): DecodedJWT
}