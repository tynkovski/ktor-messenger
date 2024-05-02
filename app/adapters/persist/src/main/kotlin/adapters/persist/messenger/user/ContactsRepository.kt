package adapters.persist.messenger.user

import adapters.persist.messenger.entities.ContactToUserEntities
import adapters.persist.messenger.entities.ContactToUserEntity
import adapters.persist.messenger.mappers.fromSqlResultRow
import adapters.persist.messenger.mappers.toSqlStatement
import adapters.persist.util.postgresql.pgInsertOrUpdate
import core.outport.MustBeCalledInTransactionContext
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import java.time.LocalDateTime

class ContactsRepository {
    @MustBeCalledInTransactionContext
    fun addToContacts(applicantId: Long, userId: Long): Boolean {
        val entity = ContactToUserEntity(
            userId = applicantId,
            contactId = userId,
            createdAt = LocalDateTime.now()
        )
        return ContactToUserEntities
            .pgInsertOrUpdate(ContactToUserEntities.userId, ContactToUserEntities.contactId) {
                entity.toSqlStatement(it)
            }
            .resultedValues!!.isNotEmpty()
    }

    @MustBeCalledInTransactionContext
    fun removeFromContacts(applicantId: Long, userId: Long): Boolean {
        return ContactToUserEntities.deleteWhere {
            (ContactToUserEntities.userId eq applicantId) and (ContactToUserEntities.contactId eq userId)
        } > 0
    }

    @MustBeCalledInTransactionContext
    fun getContacts(applicantId: Long): Collection<ContactToUserEntity> {
        return ContactToUserEntities
            .select { ContactToUserEntities.userId eq applicantId }
            .map { ContactToUserEntity.fromSqlResultRow(it) }
    }
}

