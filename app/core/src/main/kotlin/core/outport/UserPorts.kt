package core.outport

import core.models.UserEntry

interface AddUserPort {
    fun addUser(entry: UserEntry): UserEntry
}

interface GetUserPort {
    fun getUser(id: Long): UserEntry
}

interface GetUsersPort {
    fun getUsers(ids: List<Long>): Collection<UserEntry>
}

interface GetUserByLoginPort {
    fun getUser(login: String): UserEntry
}

interface DeleteUserPort {
    fun deleteUser(entry: UserEntry): UserEntry
}

interface UpdateUserPort {
    fun updateUser(entry: UserEntry): UserEntry
}

interface FindUserForAccessKeyPort {
    fun findUserForAccessKey(accessKey: String): UserEntry?
}

interface FindUserForKeysPort {
    fun findUserForKeys(accessKey: String, refreshKey: String): UserEntry?
}

fun interface AddToContactsPort {
    suspend fun addToContacts(applicantId: Long, userId: Long): Boolean
}

fun interface RemoveFromContactsPort {
    suspend fun removeFromContacts(applicantId: Long, userId: Long): Boolean
}

fun interface GetContactsPort {
    suspend fun getContacts(applicantId: Long): Collection<Long>
}

fun interface BlockUserPort {
    suspend fun blockUser(applicantId: Long, userId: Long): Boolean
}

fun interface UnblockUserPort {
    suspend fun unblockUser(applicantId: Long, userId: Long): Boolean
}

fun interface GetBlockedUsersPort {
    suspend fun getBlockedUsers(applicantId: Long): Collection<Long>
}
