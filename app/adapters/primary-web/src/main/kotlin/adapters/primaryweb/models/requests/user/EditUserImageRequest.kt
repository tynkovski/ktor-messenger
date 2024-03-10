package adapters.primaryweb.models.requests.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserImageRequest(
    @SerialName("image") val image: String?
)