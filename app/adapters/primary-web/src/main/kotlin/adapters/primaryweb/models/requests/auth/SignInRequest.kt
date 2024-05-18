package adapters.primaryweb.models.requests.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest (
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
)
