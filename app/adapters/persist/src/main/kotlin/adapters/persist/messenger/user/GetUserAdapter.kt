package adapters.persist.messenger.user

import adapters.persist.messenger.mappers.fromEntity
import core.models.UserEntry
import core.models.UserEntryNotFoundException
import core.outport.GetUserPort
import core.outport.MustBeCalledInTransactionContext

internal class GetUserAdapter(
    private val userRepository: UserRepository
) : GetUserPort {

    @MustBeCalledInTransactionContext
    override fun getUser(id: Long): UserEntry {
        val entity = userRepository.getByIdOrNull(id = id)
            ?: throw UserEntryNotFoundException(searchCriteria = "id=$id")
        return UserEntry.fromEntity(entity = entity)
    }
}