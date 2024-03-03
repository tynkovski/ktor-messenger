package core.outport

import core.security.hashing.SaltedHash

interface VerifyPasswordPort {
    fun verify(password: String, saltedHash: SaltedHash): Boolean
}

interface GenerateSaltedHashPort {
    fun generate(value: String, saltLength: Int): SaltedHash
}