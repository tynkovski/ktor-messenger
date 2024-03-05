package adapters.security

import core.outport.GenerateSaltedHashPort
import core.outport.VerifyPasswordPort
import core.security.hashing.SaltedHash
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

internal class HashingAdapter : VerifyPasswordPort, GenerateSaltedHashPort {
    override fun verify(password: String, saltedHash: SaltedHash): Boolean {
        return DigestUtils.sha256Hex(saltedHash.salt + password) == saltedHash.hash
    }

    override fun generate(value: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$saltAsHex$value")
        return SaltedHash(hash = hash, salt = saltAsHex)
    }
}
