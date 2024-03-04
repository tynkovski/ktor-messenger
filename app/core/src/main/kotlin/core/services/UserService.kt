package core.services

import core.models.UserEntry
import core.outport.*
import core.usecase.*

internal class AddUserService(
    private val addUserPort: AddUserPort,
    private val txPort: PersistTransactionPort,
) : AddUserUsecase {
    override suspend fun addUser(entry: UserEntry): UserEntry =
        txPort.withNewTransaction { addUserPort.addUser(entry) }
}

internal class GetUserService(
    private val getUserPort: GetUserPort,
    private val txPort: PersistTransactionPort,
) : GetUserUsecase {
    override suspend fun getUser(id: Long): UserEntry =
        txPort.withNewTransaction { getUserPort.getUser(id) }
}

internal class GetUserByLoginService(
    private val getUserPort: GetUserByLoginPort,
    private val txPort: PersistTransactionPort,
) : GetUserByLoginUsecase {
    override suspend fun getUser(login: String): UserEntry =
        txPort.withNewTransaction { getUserPort.getUser(login) }
}

internal class DeleteUserService(
    private val deleteUserPort: DeleteUserPort,
    private val txPort: PersistTransactionPort,
) : DeleteUserUsecase {
    override suspend fun deleteUser(id: Long) =
        txPort.withNewTransaction { deleteUserPort.deleteUser(id) }
}


internal class UpdateUserService(
    private val updateUserPort: UpdateUserPort,
    private val txPort: PersistTransactionPort,
) : UpdateUserUsecase {
    override suspend fun updateUser (entry: UserEntry): UserEntry =
        txPort.withNewTransaction { updateUserPort.updateUser(entry) }
}

internal class FindUserForAccessKeyService(
    private val findUserForAccessKeyPort: FindUserForAccessKeyPort,
    private val txPort: PersistTransactionPort,
) : FindUserForAccessKeyUsecase {
    override suspend fun findUserForAccessKey(accessKey: String) = txPort.withNewTransaction {
        findUserForAccessKeyPort.findUserForAccessKey(accessKey)
    }
}

internal class FindUserForKeysService(
    private val findUserForKeysPort: FindUserForKeysPort,
    private val txPort: PersistTransactionPort,
) : FindUserForKeysUsecase {
    override suspend fun findUserForKeys(accessKey: String, refreshKey: String) = txPort.withNewTransaction {
        findUserForKeysPort.findUserForKeys(accessKey, refreshKey)
    }
}