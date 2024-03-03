package core.services

import core.models.UserEntry
import core.outport.*
import core.usecase.AddUserUsecase
import core.usecase.DeleteUserUsecase
import core.usecase.GetUserUsecase
import core.usecase.UpdateUserUsecase

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