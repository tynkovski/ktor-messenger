package adapters.primaryweb.models.requests.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JoinToRoomRequest(
    @SerialName("roomId") val roomId: Long
)
