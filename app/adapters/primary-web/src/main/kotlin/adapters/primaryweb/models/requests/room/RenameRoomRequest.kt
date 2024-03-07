package adapters.primaryweb.models.requests.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RenameRoomRequest(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String?,
)
