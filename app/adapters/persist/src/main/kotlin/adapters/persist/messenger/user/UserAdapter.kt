package adapters.persist.messenger.user

import adapters.persist.messenger.entities.BlockedToUserEntity
import adapters.persist.messenger.entities.ContactToUserEntity
import adapters.persist.messenger.keystore.KeyStoreRepository
import adapters.persist.messenger.mappers.fromEntity
import adapters.persist.messenger.mappers.toUserSqlEntity
import core.models.UserEntry
import core.models.UserEntryNotFoundException
import core.outport.*

internal class UserAdapter(
    private val userRepository: UserRepository,
    private val keyStoreRepository: KeyStoreRepository,
    private val contactsRepository: ContactsRepository,
    private val blacklistRepository: BlacklistRepository,
) : GetUserPort,
    AddUserPort,
    DeleteUserPort,
    UpdateUserPort,
    GetUserByLoginPort,
    FindUserForKeysPort,
    FindUserForAccessKeyPort,
    GetContactsPort,
    AddToContactsPort,
    RemoveFromContactsPort,
    GetBlockedUsersPort,
    BlockUserPort,
    UnblockUserPort {

    @MustBeCalledInTransactionContext
    override fun getUser(id: Long): UserEntry {
        val entity = userRepository.getByIdOrNull(userId = id)
            ?: throw UserEntryNotFoundException(searchCriteria = "id=$id")
        return UserEntry.fromEntity(entity = entity)
    }

    @MustBeCalledInTransactionContext
    override fun getUser(login: String): UserEntry {
        val entity = userRepository.getByLoginOrNull(login = login)
            ?: throw UserEntryNotFoundException(searchCriteria = "login=$login")
        return UserEntry.fromEntity(entity = entity)
    }

    @MustBeCalledInTransactionContext
    override fun addUser(entry: UserEntry): UserEntry {
        require(entry.id == null) { "user.id must be null" }
        return upsertUserEntry(entry = entry)
    }

    @MustBeCalledInTransactionContext
    override fun updateUser(entry: UserEntry): UserEntry {
        val id = requireNotNull(entry.id) { "user.id must not be null" }
        if (!userRepository.hasEntityWithId(userId = id)) {
            throw UserEntryNotFoundException(searchCriteria = "id=$id")
        }
        return upsertUserEntry(entry = entry)
    }

    @MustBeCalledInTransactionContext
    override fun findUserForAccessKey(accessKey: String): UserEntry? {
        val keyStoreEntity = keyStoreRepository.getKeyStoreByAccessKeyOrNull(accessKey = accessKey)
        val user = keyStoreEntity?.let { userRepository.getByIdOrNull(it.userId) }
        return user?.let { UserEntry.fromEntity(entity = it) }
    }

    @MustBeCalledInTransactionContext
    override fun findUserForKeys(accessKey: String, refreshKey: String): UserEntry? {
        val keyStoreEntity = keyStoreRepository.getKeyStoreByKeysOrNull(
            accessKey = accessKey,
            refreshKey = refreshKey
        )
        val user = keyStoreEntity?.let { userRepository.getByIdOrNull(it.userId) }
        return user?.let { UserEntry.fromEntity(entity = it) }
    }

    @MustBeCalledInTransactionContext
    private fun upsertUserEntry(
        entry: UserEntry
    ): UserEntry {
        val sqlEntity = userRepository.upsert(entry.toUserSqlEntity())
        return UserEntry.fromEntity(sqlEntity)
    }

    @MustBeCalledInTransactionContext
    override fun deleteUser(entry: UserEntry): UserEntry {
        val id = requireNotNull(entry.id) { "user.id must not be null" }
        if (!userRepository.hasEntityWithId(userId = id)) {
            throw UserEntryNotFoundException(searchCriteria = "id=$id")
        }
        return upsertUserEntry(entry)
    }

    @MustBeCalledInTransactionContext
    override suspend fun addToContacts(applicantId: Long, userId: Long): Boolean {
        return contactsRepository.addToContacts(applicantId, userId)
    }

    @MustBeCalledInTransactionContext
    override suspend fun removeFromContacts(applicantId: Long, userId: Long): Boolean {
        return contactsRepository.removeFromContacts(applicantId, userId)
    }

    @MustBeCalledInTransactionContext
    override suspend fun getContacts(applicantId: Long): Collection<Long> {
        return contactsRepository
            .getContacts(applicantId)
            .map(ContactToUserEntity::contactId)
    }

    @MustBeCalledInTransactionContext
    override suspend fun blockUser(applicantId: Long, userId: Long): Boolean {
        return blacklistRepository.blockUser(applicantId, userId)
    }

    @MustBeCalledInTransactionContext
    override suspend fun unblockUser(applicantId: Long, userId: Long): Boolean {
        return blacklistRepository.unblockUser(applicantId, userId)
    }

    @MustBeCalledInTransactionContext
    override suspend fun getBlockedUsers(applicantId: Long): Collection<Long> {
        return blacklistRepository
            .getBlockedUsers(applicantId)
            .map(BlockedToUserEntity::blockedId)
    }
}