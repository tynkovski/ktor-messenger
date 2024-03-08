package adapters.primaryweb.models.responses.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteRoomResponse(
    @SerialName("roomId") val roomId: Long
)
