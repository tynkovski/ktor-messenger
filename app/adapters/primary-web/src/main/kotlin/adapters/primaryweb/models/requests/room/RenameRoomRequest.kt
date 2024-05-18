package adapters.primaryweb.models.requests.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RenameRoomRequest(
    @SerialName("roomId") val roomId: Long,
    @SerialName("name") val name: String?,
)
