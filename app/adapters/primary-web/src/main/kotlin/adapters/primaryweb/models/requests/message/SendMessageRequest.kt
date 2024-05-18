package adapters.primaryweb.models.requests.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    @SerialName("roomId") val roomId: Long,
    @SerialName("text") val text: String
)
