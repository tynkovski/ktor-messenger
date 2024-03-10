package core.outport

import core.models.UserEntry

interface AddUserPort {
    fun addUser(entry: UserEntry): UserEntry
}

interface GetUserPort {
    fun getUser(id: Long): UserEntry
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

