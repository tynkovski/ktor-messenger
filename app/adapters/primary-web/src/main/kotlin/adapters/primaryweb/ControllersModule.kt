package adapters.primaryweb

import adapters.primaryweb.controllers.AuthController
import adapters.primaryweb.controllers.MessageController
import adapters.primaryweb.controllers.RoomController
import adapters.primaryweb.controllers.UserController
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
            editUserNameUsecase = get(),
            deleteUserUsecase = get(),
            getUserUsecase = get()
        )
    }
    single {
        RoomController(
            getRoomUsecase = get(),
            addRoomUsecase = get(),
            renameRoomUsecase = get(),
            getRoomsCountUsecase = get(),
            getRoomsPagedUsecase = get()
        )
    }
    single {
        MessageController(get())
    }
}