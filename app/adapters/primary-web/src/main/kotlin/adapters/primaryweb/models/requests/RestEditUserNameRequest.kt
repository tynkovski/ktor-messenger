package adapters.primaryweb.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestEditUserNameRequest(
    @SerialName(value = "name")
    val name: String
)