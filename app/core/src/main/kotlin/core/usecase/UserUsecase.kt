package core.usecase

import core.models.UserEntry

fun interface AddUserUsecase {
    suspend fun addUser(entry: UserEntry): UserEntry
}

fun interface GetUserUsecase {
    suspend fun getUser(id: Long): UserEntry
}

fun interface DeleteUserUsecase {
    suspend fun deleteUser(id: Long)
}

fun interface UpdateUserUsecase {
    suspend fun updateUser(entry: UserEntry): UserEntry
}

fun interface FindUserForAccessKeyUsecase {
    suspend fun findUserForAccessKey(accessKey: String): UserEntry?
}
