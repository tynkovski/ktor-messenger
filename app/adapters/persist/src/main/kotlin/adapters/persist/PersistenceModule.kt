package adapters.persist

import adapters.persist.messenger.person.LoadPersonAdapter
import adapters.persist.messenger.user.GetUserAdapter
import adapters.persist.messenger.person.SavePersonAdapter
import adapters.persist.messenger.user.SaveUserAdapter
import adapters.persist.messenger.person.PersonRepository
import adapters.persist.messenger.person.PostalAddressRepository
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
        PersonRepository()
    }
    single {
        PostalAddressRepository()
    }
    single {
        UserRepository()
    }

    single {
        LoadPersonAdapter(
            personRepository = get(),
            postalAddressRepository = get(),
        )
    } binds arrayOf(
        LoadPersonPort::class,
        LoadAllPersonsPort::class,
    )
    single {
        SavePersonAdapter(
            personRepository = get(),
            postalAddressRepository = get(),
        )
    } binds arrayOf(
        AddPersonPort::class,
        UpdatePersonPort::class,
        DeletePersonPort::class,
    )

    single {
        GetUserAdapter(
            userRepository = get(),
        )
    } binds arrayOf(
        GetUserPort::class,
    )
    single {
        SaveUserAdapter(
            userRepository = get()
        )
    } binds arrayOf(
        AddUserPort::class,
        UpdateUserPort::class,
        DeleteUserPort::class,
    )
}
