package adapters.primaryweb.models.requests.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageRequest(
    @SerialName("messageId") val messageId: Long
)
