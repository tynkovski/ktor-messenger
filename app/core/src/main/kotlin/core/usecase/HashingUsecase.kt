package core.usecase

import core.security.hashing.SaltedHash

fun interface VerifyPasswordUsecase {
    suspend fun verify(password: String, saltedHash: SaltedHash): Boolean
}

fun interface GenerateSaltedHashUsecase {
    suspend fun generate(value: String, saltLength: Int): SaltedHash
}