package core.security.token

import core.models.UserEntry

data class TokenClaim(
    val name: String,
    val value: String
) {
    companion object {
        fun create(user: UserEntry) = TokenClaim("userId", "${user.id}")
    }
}