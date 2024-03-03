package adapters.persist.messenger.person

import adapters.persist.messenger.mappers.fromEntity
import com.github.michaelbull.logging.InlineLogger
import core.models.PersonEntry
import core.models.PersonEntryNotFoundException
import core.outport.LoadAllPersonsPort
import core.outport.LoadPersonPort
import core.outport.MustBeCalledInTransactionContext

/**
 * Adapter to perform load operations over address book item and postal address repositories.
 */
internal class LoadPersonAdapter(
    private val personRepository: PersonRepository,
    private val postalAddressRepository: PostalAddressRepository,
) : LoadPersonPort,
    LoadAllPersonsPort {

    private val logger = InlineLogger()

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
