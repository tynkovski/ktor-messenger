package adapters.primaryweb

import adapters.primaryweb.controllers.*
import org.koin.dsl.module

val controllersModule = module {
    single {
        AuthController(
            createAndSaveTokensUsecase = get(),
            verifyAccessTokenUsecase = get(),
            getUserForKeysUsecase = get(),
            createAccessTokenUsecase = get(),
            deleteRefreshTokenUsecase = get(),
            verifyRefreshTokenUsecase = get(),
            createUserUsecase = get(),
            createTokensUsecase = get(),
            getUserByLoginPasswordUsecase = get(),
        )
    }

    single {
        UserController(
            editUserUsecase = get(),
            getUsersUsecase = get(),
            editUserNameUsecase = get(),
            deleteUserUsecase = get(),
            getUserUsecase = get(),
            createUserUsecase = get(),
            editUserImageUsecase = get()
        )
    }
    single {
        SearchController(
            getUserUsecase = get()
        )
    }
    single {
        ContactsController(
            getContactsUsecase = get(),
            addToContactsUsecase = get(),
            removeFromContactsUsecase = get(),
            getUsersUsecase = get()
        )
    }

    single {
        RoomController(
            getUserUsecase = get(),
            createRoomUsecase = get(),
            getRoomUsecase = get(),
            getRoomsPagedUsecase = get(),
            getRoomsCountUsecase = get(),
            findRoomUsecase = get()
        )
    }

    single {
        MessageController(
            getMessageUsecase = get(),
            getMessagesPagingUsecase = get(),
            getMessageCountUsecase = get(),
        )
    }

    single {
        RealtimeChatController(
            getMessageUsecase = get(),
            getUserUsecase = get(),
            getRoomUsecase = get(),
            updateRoomUsecase = get(),
            sendMessageUsecase = get(),
            deleteMessageUsecase = get(),
            editMessageUsecase = get(),
            readMessageUsecase = get(),
            createRoomUsecase = get(),
            renameRoomUsecase = get(),
            joinToRoomUsecase = get(),
            quitFromRoomUsecase = get(),
            inviteUserToRoomUsecase = get(),
            kickUserFromRoomUsecase = get(),
            makeModeratorInRoomUsecase = get(),
            deleteRoomUsecase = get()
        )
    }
}