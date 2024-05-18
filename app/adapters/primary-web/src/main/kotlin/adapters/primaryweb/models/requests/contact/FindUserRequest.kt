package adapters.primaryweb.models.requests.contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FindUserRequest(
    @SerialName("login") val login: String
)
