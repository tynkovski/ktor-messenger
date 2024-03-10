package core.usecase

import core.models.UserEntry

fun interface CreateUserUsecase {
    suspend fun createUser(
        name: String?,
        image: String?,
        login: String,
        password: String
    ): UserEntry
}

fun interface GetUserUsecase {
    suspend fun getUser(userId: Long): UserEntry
}

fun interface GetUserByLoginPasswordUsecase {
    suspend fun getUser(login: String, password: String): UserEntry?
}

fun interface DeleteUserUsecase {
    suspend fun deleteUser(userId: Long): UserEntry
}

fun interface EditUserUsecase {
    suspend fun editUser(userId: Long, name: String?, image: String?): UserEntry
}

fun interface EditUserNameUsecase {
    suspend fun editUserName(userId: Long, name: String?): UserEntry
}

fun interface EditUserImageUsecase {
    suspend fun editUserImage(userId: Long, image: String?): UserEntry
}

fun interface FindUserForAccessKeyUsecase {
    suspend fun findUserForAccessKey(accessKey: String): UserEntry?
}

fun interface FindUserForKeysUsecase {
    suspend fun findUserForKeys(accessKey: String, refreshKey: String): UserEntry?
}
