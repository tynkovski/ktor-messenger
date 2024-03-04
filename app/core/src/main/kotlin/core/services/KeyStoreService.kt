package core.services

import core.models.UserEntry
import core.outport.CreateAccessTokenPort
import core.outport.CreateAndSaveTokensPort
import core.outport.DeleteRefreshTokenPort
import core.outport.PersistTransactionPort
import core.usecase.CreateAccessTokenUsecase
import core.usecase.CreateAndSaveTokensUsecase
import core.usecase.DeleteRefreshTokenUsecase

internal class CreateAndSaveTokensService(
    private val createAndSaveTokensPort: CreateAndSaveTokensPort,
    private val txPort: PersistTransactionPort,
) : CreateAndSaveTokensUsecase {
    override suspend fun createAndSaveTokens(user: UserEntry) = txPort.withNewTransaction {
        createAndSaveTokensPort.createAndSaveTokens(user)
    }
}

internal class CreateAccessTokenService(
    private val createAccessTokenPort: CreateAccessTokenPort,
    private val txPort: PersistTransactionPort,
) : CreateAccessTokenUsecase {
    override suspend fun createAndSaveAccessToken(user: UserEntry, refreshKey: String) = txPort.withNewTransaction {
        createAccessTokenPort.createAndSaveAccessToken(user, refreshKey)
    }
}

internal class DeleteRefreshTokenService(
    private val deleteRefreshTokenPort: DeleteRefreshTokenPort,
    private val txPort: PersistTransactionPort,
) : DeleteRefreshTokenUsecase {
    override suspend fun deleteRefreshToken(refreshKey: String) = txPort.withNewTransaction {
        deleteRefreshTokenPort.deleteRefreshToken(refreshKey)
    }
}
