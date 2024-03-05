package adapters.primaryweb.controllers

import org.koin.dsl.module

val controllersModule = module {
    single {
        AuthController(
            verifyPasswordUsecase = get(),
            getUserByLoginUsecase = get(),
            createAndSaveTokensUsecase = get(),
            verifyAccessTokenUsecase = get(),
            getUserForKeysUsecase = get(),
            createAccessTokenUsecase = get(),
            deleteRefreshTokenUsecase = get(),
            verifyRefreshTokenUsecase = get(),
            addUserUsecase = get(),
            createTokensUsecase = get(),
            saltedHashUsecase = get(),
        )
    }
    single {
        UserController(
            saltedHashUsecase = get(),
            addUserUsecase = get(),
            updateUserUsecase = get(),
            deleteUserUsecase = get()
        )
    }
    single { RoomController() }
}