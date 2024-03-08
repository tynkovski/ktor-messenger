package adapters.primaryweb.models.responses.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestTokenResponse(
    @SerialName(value = "accessToken")
    val accessToken: String,

    @SerialName(value = "refreshToken")
    val refreshToken: String
)