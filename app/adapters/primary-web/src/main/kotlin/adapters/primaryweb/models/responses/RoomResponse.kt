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
    @SerialName("lastAction") val lastAction: RoomLastActionResponse,
    @SerialName("createdAt") val createdAt: String,
)

@Serializable
data class RoomLastActionResponse(
    @SerialName("authorId") val authorId: Long,
    @SerialName("actionType") val actionType: String,
    @SerialName("description") val description: String?,
    @SerialName("actionDateTime") val actionDateTime: String,
)