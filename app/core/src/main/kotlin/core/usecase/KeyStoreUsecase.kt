package core.usecase

import core.models.UserEntry

fun interface FindUserForAccessKeyUsecase {
    suspend fun findUserForAccessKey(accessKey: String): UserEntry?
}
