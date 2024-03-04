package service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import core.outport.TokenConfig
import core.security.token.TokenClaim
import service.util.toHex
import usecase.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

internal class JwtTokenVerifierService(
    private val config: TokenConfig
) : AccessTokenVerifierUsecase, GenerateKeyUsecase,
    GenerateAccessTokenUsecase,
    GenerateRefreshTokenUsecase,
    VerifyRefreshTokenUsecase,
    VerifyAccessTokenUsecase {
    private val accessTokenAlgorithm = Algorithm.HMAC256(config.accessSecret)

    private val refreshTokenAlgorithm = Algorithm.HMAC256(config.refreshSecret)

    private val accessTokenVerifier = JWT.require(accessTokenAlgorithm)
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()

    private val refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()


    override fun generateKey(): String = Random.nextBytes(64).toHex()

    override fun accessTokenVerifier(): JWTVerifier = accessTokenVerifier

    override fun generateAccessToken(key: String, vararg claim: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(30)))
            .withClaim("key", key)

        claim.forEach { token = token.withClaim(it.name, it.value) }

        return token.sign(accessTokenAlgorithm)
    }

    override fun generateRefreshToken(key: String, vararg claim: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(180)))
            .withClaim("key", key)

        claim.forEach { token = token.withClaim(it.name, it.value) }

        return token.sign(refreshTokenAlgorithm)
    }

    override fun verifyRefreshToken(refreshToken: String): DecodedJWT {
        return refreshTokenVerifier.verify(refreshToken)
    }

    override fun verifyAccessToken(accessToken: String, ignoreExpiry: Boolean): DecodedJWT {
        return try {
            accessTokenVerifier.verify(accessToken)
        } catch (t: TokenExpiredException) {
            if (ignoreExpiry) {
                JWT.decode(accessToken)
            } else {
                throw t
            }
        } catch (t: Throwable) {
            throw t
        }
    }
}