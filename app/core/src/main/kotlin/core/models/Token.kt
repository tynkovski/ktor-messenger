package core.models

data class TokenEntry(
    val accessToken: String,
    val refreshToken: String
) {
    companion object
}