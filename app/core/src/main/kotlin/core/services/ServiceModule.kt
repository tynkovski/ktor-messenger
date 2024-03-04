package core.services

import core.usecase.*
import org.koin.dsl.binds
import org.koin.dsl.module

internal val serviceModule = module {
    single<HealthStatusUsecase> {
        HealthStatusService(getDeploymentPort = get())
    }

    single<AddPersonUsecase> {
        AddPersonService(addPersonPort = get(), txPort = get())
    }
    single<LoadPersonUsecase> {
        LoadPersonService(loadPersonPort = get(), txPort = get())
    }
    single<DeletePersonUsecase> {
        DeletePersonService(deletePersonPort = get(), txPort = get())
    }
    single<UpdatePersonUsecase> {
        UpdatePersonService(updatePersonPort = get(), txPort = get())
    }
    single<LoadAllPersonsUsecase> {
        LoadAllPersonsService(loadAllPersonsPort = get(), txPort = get())
    }
    single<RandomPersonUsecase> {
        RandomPersonService(
            generateRandomPersonPort = get(),
            addPersonPort = get(),
            txPort = get(),
        )
    }

    single<AddUserUsecase> {
        AddUserService(addUserPort = get(), txPort = get())
    }
    single<GetUserUsecase> {
        GetUserService(getUserPort = get(), txPort = get())
    }
    single<DeleteUserUsecase> {
        DeleteUserService(deleteUserPort = get(), txPort = get())
    }
    single<UpdateUserUsecase> {
        UpdateUserService(updateUserPort = get(), txPort = get())
    }

    single<VerifyPasswordUsecase> {
        VerifyPasswordService(verifyPasswordPort = get())
    }

    single<GenerateSaltedHashUsecase> {
        GenerateSaltedHashService(generateSaltedHashPort= get())
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
}
