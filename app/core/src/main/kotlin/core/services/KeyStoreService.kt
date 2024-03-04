package core.services

import core.outport.FindUserForAccessKeyPort
import core.outport.PersistTransactionPort
import core.usecase.FindUserForAccessKeyUsecase

internal class FindUserForAccessKeyService(
    private val findUserForAccessKeyPort: FindUserForAccessKeyPort,
    private val txPort: PersistTransactionPort,
) : FindUserForAccessKeyUsecase {
    override suspend fun findUserForAccessKey(accessKey: String) = txPort.withNewTransaction {
        findUserForAccessKeyPort.findUserForAccessKey(accessKey)
    }
}