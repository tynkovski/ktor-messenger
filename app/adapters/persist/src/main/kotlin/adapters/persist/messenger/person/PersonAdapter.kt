package adapters.persist.messenger.person

import adapters.persist.messenger.mappers.fromEntity
import adapters.persist.messenger.mappers.toPersonSqlEntity
import adapters.persist.messenger.mappers.toPostalAddressSqlEntity
import com.github.michaelbull.logging.InlineLogger
import core.models.PersonEntry
import core.models.PersonEntryNotFoundException
import core.outport.*

/**
 * Adapter to perform save/delete operations over address book item and postal address repositories.
 */
internal class PersonAdapter(
    private val personRepository: PersonRepository,
    private val postalAddressRepository: PostalAddressRepository,
) : AddPersonPort,
    UpdatePersonPort,
    DeletePersonPort,
    LoadPersonPort,
    LoadAllPersonsPort {

    private val logger = InlineLogger()

    @MustBeCalledInTransactionContext
    override fun addPerson(entry: PersonEntry): PersonEntry {
        logger.debug { "addPersonEntry(): Add person entry: $entry" }
        require(entry.id == null) { "entry.id must be null" }
        return upsertPersonEntry(personEntry = entry, postalAddressId = null)
    }

    @MustBeCalledInTransactionContext
    override fun updatePerson(entry: PersonEntry): PersonEntry {
        logger.debug { "updatePersonEntry(): Update person entry: $entry" }
        val personId = requireNotNull(entry.id) { "entity.id must not be null" }
        if (!personRepository.hasEntityWithId(id = personId)) {
            throw PersonEntryNotFoundException(searchCriteria = "id=$personId")
        }
        val postalAddressId = postalAddressRepository
            .getByPersonIdOrNull(personId)
            ?.id
        return upsertPersonEntry(
            personEntry = entry,
            postalAddressId = postalAddressId,
        )
    }

    @MustBeCalledInTransactionContext
    private fun upsertPersonEntry(
        personEntry: PersonEntry,
        postalAddressId: Long?,
    ): PersonEntry {
        val addressBookItemSqlEntity = personRepository.upsert(personEntry.toPersonSqlEntity())
        val postalAddressSqlEntity = personEntry.postalAddress
            ?.toPostalAddressSqlEntity(
                personId = addressBookItemSqlEntity.id!!,
                postalAddressId = postalAddressId,
            )
            ?.let {
                postalAddressRepository.upsert(it)
            }
        return PersonEntry.fromEntity(
            personSqlEntity = addressBookItemSqlEntity,
            postalAddressSqlEntity = postalAddressSqlEntity,
        )
    }

    @MustBeCalledInTransactionContext
    override fun deletePerson(id: Long) {
        logger.debug { "deletePersonEntry(): Delete person entity by id=$id" }
        if (!personRepository.deleteById(id = id)) {
            throw PersonEntryNotFoundException(searchCriteria = "id=$id")
        }
    }

    @MustBeCalledInTransactionContext
    override fun loadPerson(id: Long): PersonEntry {
        logger.debug { "loadPerson(): Load person entry: id=$id" }
        val personSqlEntity = personRepository.getByIdOrNull(id = id)
            ?: throw PersonEntryNotFoundException(searchCriteria = "id=$id")
        val postalAddressSqlEntity = postalAddressRepository.getByPersonIdOrNull(id)
        return PersonEntry.fromEntity(
            personSqlEntity = personSqlEntity,
            postalAddressSqlEntity = postalAddressSqlEntity,
        )
    }

    @MustBeCalledInTransactionContext
    override fun loadAllPersons(): Collection<PersonEntry> {
        logger.debug { "loadAllPersons(): Load all person entries" }
        val personSqlEntities = personRepository.getAll()
        val postalAddressSqlEntitiesMap = postalAddressRepository.getAll().associateBy {
            it.personId
        }
        return personSqlEntities
            .map { personSqlEntity ->
                val postalAddressSqlEntity = postalAddressSqlEntitiesMap[personSqlEntity.id!!]
                PersonEntry.fromEntity(
                    personSqlEntity = personSqlEntity,
                    postalAddressSqlEntity = postalAddressSqlEntity,
                )
            }
    }
}
