package adapters.primaryweb.models.requests.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KickUserFromRoomRequest(
    @SerialName("roomId") val roomId: Long,
    @SerialName("userId") val userId: Long
)
