package adapters.primaryweb.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?,
    @SerialName("users") val users: List<Long>,
    @SerialName("moderators") val moderators: List<Long>,
    @SerialName("createdAt") val createdAt: String,
)