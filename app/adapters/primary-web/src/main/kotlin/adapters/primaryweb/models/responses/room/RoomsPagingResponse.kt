package adapters.primaryweb.models.responses.room

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomsPagingResponse(
    @SerialName("page") val page: Long,
    @SerialName("total_count") val count: Long,
    @SerialName("rooms") val rooms: List<RoomResponse>
)
