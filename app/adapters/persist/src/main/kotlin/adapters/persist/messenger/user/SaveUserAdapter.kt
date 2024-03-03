package adapters.persist.messenger.user

import adapters.persist.messenger.mappers.fromEntity
import adapters.persist.messenger.mappers.toUserSqlEntity
import core.models.UserEntry
import core.models.UserEntryNotFoundException
import core.outport.AddUserPort
import core.outport.DeleteUserPort
import core.outport.MustBeCalledInTransactionContext
import core.outport.UpdateUserPort

internal class SaveUserAdapter(
    private val userRepository: UserRepository
) : AddUserPort, DeleteUserPort, UpdateUserPort {
    @MustBeCalledInTransactionContext
    override fun addUser(entry: UserEntry): UserEntry {
        require(entry.id == null) { "entry.id must be null" }
        return upsertUserEntry(entry = entry)
    }

    @MustBeCalledInTransactionContext
    override fun updateUser(entry: UserEntry): UserEntry {
        val id = requireNotNull(entry.id) { "entity.id must not be null" }
        if (!userRepository.hasEntityWithId(id = id)) {
            throw UserEntryNotFoundException(searchCriteria = "id=$id")
        }
        return upsertUserEntry(entry = entry)
    }

    @MustBeCalledInTransactionContext
    override fun deleteUser(id: Long) {
        if (!userRepository.deleteById(id)) {
            throw UserEntryNotFoundException(searchCriteria = "id=$id")
        }
    }

    @MustBeCalledInTransactionContext
    private fun upsertUserEntry(
        entry: UserEntry
    ): UserEntry {
        val sqlEntity = userRepository.upsert(entry.toUserSqlEntity())
        return UserEntry.fromEntity(sqlEntity)
    }
}