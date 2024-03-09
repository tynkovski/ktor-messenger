package adapters.primaryweb.models.requests.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMessageRequest(
    @SerialName("messageId") val messageId: Long,
    @SerialName("text") val text: String
)
