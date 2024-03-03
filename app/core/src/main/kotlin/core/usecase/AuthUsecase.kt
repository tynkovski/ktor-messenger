package core.usecase

// todo create auth

fun interface LoginUserUsecase {
    suspend fun loginUser()
}

fun interface LogoutUserUsecase {
    suspend fun logoutUser()
}

fun interface RefreshTokenUsecase {
    suspend fun refreshToken()
}