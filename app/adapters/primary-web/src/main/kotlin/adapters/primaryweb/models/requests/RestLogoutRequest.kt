package adapters.primaryweb.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestLogoutRequest(
    @SerialName("refreshToken")
    val refreshToken: String
)