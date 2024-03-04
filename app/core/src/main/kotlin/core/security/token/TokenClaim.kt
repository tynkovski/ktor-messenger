package core.security.token

import core.models.UserEntry

data class TokenClaim(
    val name: String,
    val value: String
) {
    companion object {
        const val TAG = "user_id"
        fun create(user: UserEntry) = TokenClaim(TAG, "${user.id}")
    }
}