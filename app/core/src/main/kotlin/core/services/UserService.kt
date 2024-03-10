package core.services

import com.github.michaelbull.logging.InlineLogger
import core.models.UserEntry
import core.outport.*
import core.security.hashing.SaltedHash
import core.usecase.*
import java.time.LocalDateTime

private fun buildUser(
    name: String?,
    image: String?,
    login: String,
    hash: String,
    salt: String,
) = UserEntry(
    id = null,
    login = login,
    password = hash,
    salt = salt,
    name = name,
    image = image,
    createdAt = LocalDateTime.now(),
    deletedAt = null,
)

private fun changeName(
    user: UserEntry,
    name: String?
) = user.copy(name = name)

private fun changeImage(
    user: UserEntry,
    image: String?
) = user.copy(image = image)

private fun editUser(
    user: UserEntry,
    name: String?,
    image: String?
) = user.copy(name = name, image = image)

private fun clearUser(
    user: UserEntry
) = user.copy(
    deletedAt = LocalDateTime.now(),
    name = "[deleted user]",
    image = null
)

internal class CreateUserService(
    private val addUserPort: AddUserPort,
    private val generateSaltedHashPort: GenerateSaltedHashPort,
    private val txPort: PersistTransactionPort,
) : CreateUserUsecase {
    override suspend fun createUser(
        name: String?,
        image: String?,
        login: String,
        password: String,
    ): UserEntry = txPort.withNewTransaction {
        val saltedHash = generateSaltedHashPort.generate(password, 32)

        val user = buildUser(
            name = name,
            image = image,
            login = login,
            hash = saltedHash.hash,
            salt = saltedHash.salt
        )

        addUserPort.addUser(user)
    }
}

internal class GetUserService(
    private val getUserPort: GetUserPort,
    private val txPort: PersistTransactionPort,
) : GetUserUsecase {
    override suspend fun getUser(userId: Long): UserEntry =
        txPort.withNewTransaction { getUserPort.getUser(userId) }
}

internal class DeleteUserService(
    private val getUserPort: GetUserPort,
    private val deleteUserPort: DeleteUserPort,
    private val txPort: PersistTransactionPort,
) : DeleteUserUsecase {
    override suspend fun deleteUser(userId: Long): UserEntry =
        txPort.withNewTransaction {
            val user = getUserPort.getUser(userId)
            val deleted = clearUser(user)
            deleteUserPort.deleteUser(deleted)
        }
}

internal class GetUserByLoginPasswordService(
    private val getUserByLoginPort: GetUserByLoginPort,
    private val verifyPasswordPort: VerifyPasswordPort,
    private val txPort: PersistTransactionPort,
) : GetUserByLoginPasswordUsecase {
    override suspend fun getUser(login: String, password: String): UserEntry? = txPort.withNewTransaction {
        val user = getUserByLoginPort.getUser(login)
        InlineLogger().debug { "$user" }
        val saltedHash = SaltedHash(user.password, user.salt)
        if (verifyPasswordPort.verify(password, saltedHash)) user else null
    }
}

internal class EditUserNameService(
    private val getUserPort: GetUserPort,
    private val updateUserPort: UpdateUserPort,
    private val txPort: PersistTransactionPort,
) : EditUserNameUsecase {
    override suspend fun editUserName(userId: Long, name: String?): UserEntry =
        txPort.withNewTransaction {
            val user = getUserPort.getUser(userId)
            updateUserPort.updateUser(changeName(user = user, name = name))
        }
}

internal class EditUserImageService(
    private val getUserPort: GetUserPort,
    private val updateUserPort: UpdateUserPort,
    private val txPort: PersistTransactionPort,
) : EditUserImageUsecase {
    override suspend fun editUserImage(userId: Long, image: String?): UserEntry =
        txPort.withNewTransaction {
            val user = getUserPort.getUser(userId)
            updateUserPort.updateUser(changeImage(user = user, image = image))
        }
}

internal class EditUserService(
    private val getUserPort: GetUserPort,
    private val updateUserPort: UpdateUserPort,
    private val txPort: PersistTransactionPort,
) : EditUserUsecase {
    override suspend fun editUser(userId: Long, name: String?, image: String?): UserEntry =
        txPort.withNewTransaction {
            val user = getUserPort.getUser(userId)
            updateUserPort.updateUser(editUser(user = user, name = name, image = image))
        }
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