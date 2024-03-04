package core.outport

import core.models.TokenEntry
import core.models.UserEntry

interface CreateAndSaveTokensPort {
    fun createAndSaveTokens(user: UserEntry): TokenEntry
}

interface CreateAccessTokenPort {
    fun createAndSaveAccessToken(user: UserEntry, refreshKey: String): String
}

interface DeleteRefreshTokenPort {
    fun deleteRefreshToken(refreshKey: String)
}