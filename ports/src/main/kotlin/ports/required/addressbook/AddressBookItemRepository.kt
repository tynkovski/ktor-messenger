package ports.required.addressbook

import ports.required.RequiresTransactionContext

interface AddressBookItemRepository {
    @RequiresTransactionContext
    suspend fun getByIdOrNull(id: Long): AddressBookItem?

    @RequiresTransactionContext
    suspend fun upsert(entity: AddressBookItem): AddressBookItem

    @RequiresTransactionContext
    suspend fun getAll(): Map<Long, AddressBookItem>

    @RequiresTransactionContext
    suspend fun deleteById(id: Long): Boolean
}
