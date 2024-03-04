package service

import org.koin.dsl.binds
import org.koin.dsl.module
import usecase.*

val serviceModule = module {
    single {
        JwtTokenVerifierService(config = get())
    } binds arrayOf(
        GenerateKeyUsecase::class,
        GenerateAccessTokenUsecase::class,
        GenerateRefreshTokenUsecase::class,
        VerifyRefreshTokenUsecase::class,
        VerifyAccessTokenUsecase::class
    )
}