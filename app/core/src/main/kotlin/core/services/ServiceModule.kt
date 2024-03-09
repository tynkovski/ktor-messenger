package core.services

import core.usecase.*
import org.koin.dsl.binds
import org.koin.dsl.module

internal val serviceModule = module {
    single<HealthStatusUsecase> {
        HealthStatusService(getDeploymentPort = get())
    }

    single<AddPersonUsecase> {
        AddPersonService(
            addPersonPort = get(),
            txPort = get()
        )
    }
    single<LoadPersonUsecase> {
        LoadPersonService(
            loadPersonPort = get(),
            txPort = get()
        )
    }
    single<DeletePersonUsecase> {
        DeletePersonService(
            deletePersonPort = get(),
            txPort = get()
        )
    }
    single<UpdatePersonUsecase> {
        UpdatePersonService(
            updatePersonPort = get(),
            txPort = get()
        )
    }
    single<LoadAllPersonsUsecase> {
        LoadAllPersonsService(
            loadAllPersonsPort = get(),
            txPort = get()
        )
    }
    single<RandomPersonUsecase> {
        RandomPersonService(
            generateRandomPersonPort = get(),
            addPersonPort = get(),
            txPort = get(),
        )
    }

    single { JwtTokenService(configPort = get()) } binds arrayOf(
        AccessTokenVerifierUsecase::class,
        GenerateKeyUsecase::class,
        GenerateAccessTokenUsecase::class,
        GenerateRefreshTokenUsecase::class,
        VerifyRefreshTokenUsecase::class,
        VerifyAccessTokenUsecase::class
    )

    single<AddUserUsecase> {
        AddUserService(
            addUserPort = get(),
            txPort = get()
        )
    }
    single<GetUserUsecase> {
        GetUserService(
            getUserPort = get(),
            txPort = get()
        )
    }
    single<GetUserByLoginUsecase> {
        GetUserByLoginService(
            getUserPort = get(),
            txPort = get()
        )
    }
    single<DeleteUserUsecase> {
        DeleteUserService(
            deleteUserPort = get(),
            txPort = get()
        )
    }
    single<UpdateUserUsecase> {
        UpdateUserService(
            updateUserPort = get(),
            txPort = get()
        )
    }
    single<EditUserNameUsecase> {
        EditUserNameService(
            updateUserPort = get(),
            txPort = get()
        )
    }

    single<VerifyPasswordUsecase> {
        VerifyPasswordService(verifyPasswordPort = get())
    }
    single<GenerateSaltedHashUsecase> {
        GenerateSaltedHashService(generateSaltedHashPort = get())
    }

    single<FindUserForAccessKeyUsecase> {
        FindUserForAccessKeyService(
            findUserForAccessKeyPort = get(),
            txPort = get()
        )
    }
    single<FindUserForKeysUsecase> {
        FindUserForKeysService(
            findUserForKeysPort = get(),
            txPort = get()
        )
    }
    single<CreateAndSaveTokensUsecase> {
        CreateAndSaveTokensService(
            createAndSaveTokensPort = get(),
            txPort = get()
        )
    }
    single<CreateAccessTokenUsecase> {
        CreateAccessTokenService(
            createAccessTokenPort = get(),
            txPort = get()
        )
    }
    single<DeleteRefreshTokenUsecase> {
        DeleteRefreshTokenService(
            deleteRefreshTokenPort = get(),
            txPort = get()
        )
    }

    single<CreateRoomUsecase> {
        CreateRoomService(
            addRoomPort = get(),
            txPort = get()
        )
    }
    single<GetRoomUsecase> {
        GetRoomService(
            getRoomPort = get(),
            txPort = get()
        )
    }
    single<GetRoomCountUsecase> {
        GetRoomCountService(
            getRoomCountPort = get(),
            txPort = get()
        )
    }
    single<GetRoomUsersUsecase> {
        GetRoomUsersService(
            getRoomPort = get(),
            txPort = get()
        )
    }
    single<GetRoomsPagingUsecase> {
        GetRoomsPagingService(
            getRoomsPort = get(),
            txPort = get()
        )
    }
    single<RenameRoomUsecase> {
        RenameRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<DeleteRoomUsecase> {
        DeleteRoomService(
            deleteRoomPort = get(),
            txPort = get()
        )
    }
    single<JoinToRoomUsecase> {
        JoinToRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<QuitFromRoomUsecase> {
        QuitFromRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<KickUserFromRoomUsecase> {
        KickUserFromRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<InviteUserToRoomUsecase> {
        InviteUserToRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<MakeModeratorInRoomUsecase> {
        MakeModeratorInRoomService(
            getRoomPort = get(),
            updateRoomPort = get(),
            txPort = get()
        )
    }

    single<SendMessageUsecase> {
        SendMessageService(
            addMessagePort = get(),
            txPort = get()
        )
    }
    single<GetMessageUsecase> {
        GetMessageService(
            getMessagePort = get(),
            txPort = get()
        )
    }
    single<GetMessagesPagingUsecase> {
        GetMessagesPagingService(
            getMessagesPort = get(),
            txPort = get()
        )
    }
    single<GetMessageCountUsecase> {
        GetMessageCountService(
            getMessageCountPort = get(),
            txPort = get()
        )
    }
    single<DeleteMessageUsecase> {
        DeleteMessageService(
            deleteMessagePort = get(),
            txPort = get()
        )
    }
    single<EditMessageUsecase> {
        EditMessageService(
            getMessagePort= get(),
            updateMessagePort = get(),
            txPort = get()
        )
    }
    single<ReadMessageUsecase> {
        ReadMessageService(
            getMessagePort = get(),
            updateMessagePort = get(),
            txPort = get()
        )
    }
}
