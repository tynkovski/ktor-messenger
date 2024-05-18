package adapters.persist.messenger.keystore

import adapters.persist.messenger.mappers.toUserSqlEntity
import core.models.KeyStoreEntry
import core.models.KeyStoreEntryNotFoundException
import core.models.TokenEntry
import core.models.UserEntry
import core.outport.CreateAccessTokenPort
import core.outport.CreateAndSaveTokensPort
import core.outport.DeleteRefreshTokenPort
import core.outport.MustBeCalledInTransactionContext
import core.security.token.TokenClaim
import core.usecase.GenerateAccessTokenUsecase
import core.usecase.GenerateKeyUsecase
import core.usecase.GenerateRefreshTokenUsecase
import java.time.LocalDateTime

internal class KeyStoreAdapter(
    private val keyStoreRepository: KeyStoreRepository,
    private val generateKeyUsecase: GenerateKeyUsecase,
    private val generateAccessTokenUsecase: GenerateAccessTokenUsecase,
    private val generateRefreshTokenUsecase: GenerateRefreshTokenUsecase,
) : CreateAndSaveTokensPort, CreateAccessTokenPort, DeleteRefreshTokenPort {

    @MustBeCalledInTransactionContext
    override fun createAndSaveTokens(user: UserEntry): TokenEntry {
        val randAccessKey = generateKeyUsecase.generateKey()
        val randRefreshKey = generateKeyUsecase.generateKey()
        val keyStore = KeyStoreEntry(
            id = null,
            userId = user.id!!,
            accessKey = randAccessKey,
            refreshKey = randRefreshKey
        )
        val savedKeyEntity = keyStoreRepository.upsert(keyStore.toUserSqlEntity())
        val tokenClaim = TokenClaim.create(user)
        val token = TokenEntry(
            accessToken = generateAccessTokenUsecase.generateAccessToken(randAccessKey, tokenClaim),
            refreshToken = generateRefreshTokenUsecase.generateRefreshToken(randRefreshKey, tokenClaim)
        )
        return token
    }

    @MustBeCalledInTransactionContext
    override fun createAndSaveAccessToken(user: UserEntry, refreshKey: String): String {
        val keyStore = keyStoreRepository.getKeyStoreByRefreshKeyOrNull(refreshKey = refreshKey)
            ?: throw KeyStoreEntryNotFoundException(searchCriteria = "refreshKey=$refreshKey")

        val randAccessKey = generateKeyUsecase.generateKey()
        val savedKeyEntity = keyStoreRepository.upsert( // todo we can update refresh token here
            keyStore.copy(
                accessKey = randAccessKey,
                createdAt = LocalDateTime.now()
            )
        )
        val tokenClaim = TokenClaim.create(user)
        return generateAccessTokenUsecase.generateAccessToken(randAccessKey, tokenClaim)
    }

    @MustBeCalledInTransactionContext
    override fun deleteRefreshToken(refreshKey: String) {
        val keyStore = keyStoreRepository.getKeyStoreByRefreshKeyOrNull(refreshKey = refreshKey)

        if (keyStore == null || !keyStoreRepository.deleteById(keyStore.id!!)) {
            throw KeyStoreEntryNotFoundException(searchCriteria = "refreshKey=$refreshKey")
        }
    }
}

