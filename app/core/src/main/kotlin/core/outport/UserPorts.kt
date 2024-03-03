package core.outport

import core.models.UserEntry

interface AddUserPort {
    fun addUser(entry: UserEntry): UserEntry
}

interface GetUserPort {
    fun getUser(id: Long): UserEntry
}

interface DeleteUserPort {
    fun deleteUser(id: Long)
}

interface UpdateUserPort {
    fun updateUser(entry: UserEntry): UserEntry
}

