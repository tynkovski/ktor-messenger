package adapters.primaryweb.models.requests.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserNameRequest(
    @SerialName("name") val name: String?
)