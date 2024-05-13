package adapters.persist

import adapters.persist.messenger.keystore.KeyStoreAdapter
import adapters.persist.messenger.keystore.KeyStoreRepository
import adapters.persist.messenger.message.MessageAdapter
import adapters.persist.messenger.message.MessageRepository
import adapters.persist.messenger.message.ReaderToMessageRepository
import adapters.persist.messenger.person.PersonAdapter
import adapters.persist.messenger.person.PersonRepository
import adapters.persist.messenger.person.PostalAddressRepository
import adapters.persist.messenger.room.*
import adapters.persist.messenger.room.ModeratorToRoomRepository
import adapters.persist.messenger.room.RoomAdapter
import adapters.persist.messenger.room.RoomRepository
import adapters.persist.messenger.room.UserToRoomRepository
import adapters.persist.messenger.user.BlacklistRepository
import adapters.persist.messenger.user.ContactsRepository
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
        DatabaseConnector(
            databaseConfig = get<GetDatabaseConfigPort>().database,
            errorInspector = get(),
        )
    } binds arrayOf(
        BootPersistStoragePort::class,
        ShutdownPersistStoragePort::class,
        PersistTransactionPort::class,
        ClearPersistStoragePort::class
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
        ContactsRepository()
    }
    single {
        BlacklistRepository()
    }
    single {
        KeyStoreRepository()
    }

    single {
        RoomRepository()
    }
    single {
        UserToRoomRepository()
    }
    single {
        ModeratorToRoomRepository()
    }
    single {
        ActionToRoomRepository()
    }

    single {
        MessageRepository()
    }
    single {
        ReaderToMessageRepository()
    }

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
            keyStoreRepository = get(),
            contactsRepository = get(),
            blacklistRepository = get()
        )
    } binds arrayOf(
        GetUserPort::class,
        GetUsersPort::class,
        AddUserPort::class,
        UpdateUserPort::class,
        DeleteUserPort::class,
        GetUserByLoginPort::class,
        FindUserForAccessKeyPort::class,
        FindUserForKeysPort::class,
        AddToContactsPort::class,
        RemoveFromContactsPort::class,
        BlockUserPort::class,
        UnblockUserPort::class,
        GetContactsPort::class,
        GetBlockedUsersPort::class,
    )

    single {
        RoomAdapter(
            roomRepository = get(),
            userToRoomRepository = get(),
            moderatorToRoomRepository = get(),
            actionToRoomRepository = get()
        )
    } binds arrayOf(
        AddRoomPort::class,
        GetRoomPort::class,
        GetRoomCountPort::class,
        GetRoomsPagingPort::class,
        UpdateRoomPort::class,
        DeleteRoomPort::class,
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

    single {
        MessageAdapter(
            messageRepository = get(),
            readerToMessageRepository = get()
        )
    } binds arrayOf(
        AddMessagePort::class,
        GetMessagePort::class,
        GetLastMessagePort::class,
        GetMessageCountPort::class,
        GetMessagesPagingPort::class,
        UpdateMessagePort::class,
        DeleteMessagePort::class,
        GetUnreadMessagesPort::class
    )
}
