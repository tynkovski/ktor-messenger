package adapters.primaryweb.models.requests.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRoomRequest(
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?,
    @SerialName("users") val users: List<Long>
)
