package adapters.primaryweb.models.responses.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessResponse (
    @SerialName("accessToken") val accessToken: String
)