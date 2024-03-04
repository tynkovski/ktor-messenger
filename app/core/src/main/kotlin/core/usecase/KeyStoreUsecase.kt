package core.usecase

import core.models.TokenEntry
import core.models.UserEntry

fun interface CreateAndSaveTokensUsecase {
    suspend fun createAndSaveTokens(user: UserEntry): TokenEntry
}

fun interface CreateAccessTokenUsecase {
    suspend fun createAndSaveAccessToken(user: UserEntry, refreshKey: String): String
}

fun interface DeleteRefreshTokenUsecase {
    suspend fun deleteRefreshToken(refreshKey: String)
}