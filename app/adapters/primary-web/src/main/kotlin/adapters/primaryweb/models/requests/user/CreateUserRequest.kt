package adapters.primaryweb.models.requests.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest (
    @SerialName("login") val login: String,
    @SerialName("password") val password: String,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?,
)
