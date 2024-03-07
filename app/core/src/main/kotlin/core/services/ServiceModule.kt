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

    single {
        JwtTokenService(configPort = get())
    } binds arrayOf(
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

    single<AddRoomUsecase> {
        AddRoomService(
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
    single<GetRoomsPagingUsecase> {
        GetRoomsPagingService(
            getRoomsPort = get(),
            txPort = get()
        )
    }
    single<UpdateRoomUsecase> {
        UpdateRoomService(
            updateRoomPort = get(),
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
    single<AddUserToRoomUsecase> {
        AddUserToRoomService(
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<RemoveUserFromRoomUsecase> {
        RemoveUserFromRoomService(
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<AddModeratorToRoomUsecase> {
        AddModeratorToRoomService(
            updateRoomPort = get(),
            txPort = get()
        )
    }
    single<RemoveModeratorFromRoomUsecase> {
        RemoveModeratorFromRoomService(
            updateRoomPort = get(),
            txPort = get()
        )
    }
}
