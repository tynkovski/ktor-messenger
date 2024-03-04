package core.services

import core.outport.GenerateSaltedHashPort
import core.outport.VerifyPasswordPort
import core.security.hashing.SaltedHash
import core.usecase.GenerateSaltedHashUsecase
import core.usecase.VerifyPasswordUsecase

internal class VerifyPasswordService(
    private val verifyPasswordPort: VerifyPasswordPort,
): VerifyPasswordUsecase {
    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        return verifyPasswordPort.verify(password, saltedHash)
    }
}

internal class GenerateSaltedHashService(
    private val generateSaltedHashPort: GenerateSaltedHashPort,
): GenerateSaltedHashUsecase {
    override fun generate(value: String, saltLength: Int): SaltedHash {
        return generateSaltedHashPort.generate(value, saltLength)
    }
}