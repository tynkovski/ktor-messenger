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
            addRoomUsecase = get(),
            getRoomUsecase = get(),
            deleteRoomUsecase = get(),
            renameRoomUsecase = get(),
            joinToRoomUsecase = get(),
            quitFromRoomUsecase = get(),
            inviteUserToRoomUsecase = get(),
            kickUserFromRoomUsecase = get(),
            makeModeratorInRoomUsecase = get(),
            getRoomsPagedUsecase = get(),
            getRoomsCountUsecase = get(),
        )
    }

    single {
        MessageController(
            getMessageUsecase = get(),
            getMessagesPagingUsecase = get(),
            getMessageCountUsecase = get(),
            addMessageMessageUsecase = get(),
            deleteMessageUsecase = get(),
            editMessageUsecase = get(),
            readMessageUsecase = get()
        )
    }
}