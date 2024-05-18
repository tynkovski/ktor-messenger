package adapters.primaryweb.models.responses.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesPagingResponse(
    @SerialName("roomId") val roomId: Long,
    @SerialName("page") val page: Long,
    @SerialName("totalCount") val count: Long,
    @SerialName("messages") val messages: List<MessageResponse>
)