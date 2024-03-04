package adapters.primaryweb.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestAccessResponse (
    @SerialName(value = "accessToken")
    val accessToken: String
)