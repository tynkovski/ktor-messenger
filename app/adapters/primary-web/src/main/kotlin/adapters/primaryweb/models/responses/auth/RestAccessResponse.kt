package adapters.primaryweb.models.responses.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestAccessResponse (
    @SerialName(value = "accessToken")
    val accessToken: String
)