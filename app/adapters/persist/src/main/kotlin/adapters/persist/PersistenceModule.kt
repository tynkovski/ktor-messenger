package adapters.persist

import adapters.persist.messenger.keystore.KeyStoreAdapter
import adapters.persist.messenger.keystore.KeyStoreRepository
import adapters.persist.messenger.person.PersonAdapter
import adapters.persist.messenger.person.PersonRepository
import adapters.persist.messenger.person.PostalAddressRepository
import adapters.persist.messenger.user.UserAdapter
import adapters.persist.messenger.user.UserRepository
import adapters.persist.util.DatabaseErrorInspector
import adapters.persist.util.postgresql.PgErrorInspector
import core.outport.*
import org.koin.dsl.binds
import org.koin.dsl.module

val persistenceModule = module {
    single<DatabaseErrorInspector> {
        PgErrorInspector()
    }

    single {
        PersonRepository()
    }

    single {
        PostalAddressRepository()
    }

    single {
        UserRepository()
    }

    single {
        KeyStoreRepository()
    }

    single {
        DatabaseConnector(
            databaseConfig = get<GetDatabaseConfigPort>().database,
            errorInspector = get(),
        )
    } binds arrayOf(
        BootPersistStoragePort::class,
        ShutdownPersistStoragePort::class,
        PersistTransactionPort::class,
    )

    single {
        PersonAdapter(
            personRepository = get(),
            postalAddressRepository = get(),
        )
    } binds arrayOf(
        LoadPersonPort::class,
        LoadAllPersonsPort::class,
        AddPersonPort::class,
        UpdatePersonPort::class,
        DeletePersonPort::class,
    )

    single {
        UserAdapter(
            userRepository = get(),
            keyStoreRepository = get()
        )
    } binds arrayOf(
        GetUserPort::class,
        AddUserPort::class,
        UpdateUserPort::class,
        DeleteUserPort::class,
        GetUserByLoginPort::class,
        FindUserForAccessKeyPort::class,
        FindUserForKeysPort::class
    )

    single {
        KeyStoreAdapter(
            keyStoreRepository = get(),
            generateKeyUsecase = get(),
            generateAccessTokenUsecase = get(),
            generateRefreshTokenUsecase = get(),
        )
    } binds arrayOf(
        CreateAndSaveTokensPort::class,
        CreateAccessTokenPort::class,
        DeleteRefreshTokenPort::class
    )
}
