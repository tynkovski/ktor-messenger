package adapters.persist.messenger.keystore

import adapters.persist.messenger.mappers.fromEntity
import adapters.persist.messenger.user.UserRepository
import core.models.UserEntry
import core.outport.FindUserForAccessKeyPort
import core.outport.MustBeCalledInTransactionContext

internal class KeyStoreAdapter(
    private val keyStoreRepository: KeyStoreRepository,
    private val userRepository: UserRepository,
) : FindUserForAccessKeyPort {
    @MustBeCalledInTransactionContext
    override fun findUserForAccessKey(accessKey: String): UserEntry? {
        val keyStoreEntity = keyStoreRepository.getByAccessKeyOrNull(accessKey = accessKey)
        val user = keyStoreEntity?.let { userRepository.getByIdOrNull(it.userId) }
        return user?.let { UserEntry.fromEntity(entity = it) }
    }
}