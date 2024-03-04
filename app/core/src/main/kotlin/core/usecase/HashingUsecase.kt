package core.usecase

import core.security.hashing.SaltedHash

fun interface VerifyPasswordUsecase {
    fun verify(password: String, saltedHash: SaltedHash): Boolean
}

fun interface GenerateSaltedHashUsecase {
    fun generate(value: String, saltLength: Int): SaltedHash
}