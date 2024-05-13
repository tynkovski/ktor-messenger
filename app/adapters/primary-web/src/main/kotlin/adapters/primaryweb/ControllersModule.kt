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
            createRoomUsecase = get(),
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
            sendMessageUsecase = get(),
            deleteMessageUsecase = get(),
            editMessageUsecase = get(),
            readMessageUsecase = get(),
            getRoomUsecase = get(),
            getUnreadMessagesForRoomUsecase = get(),
        )
    }
}