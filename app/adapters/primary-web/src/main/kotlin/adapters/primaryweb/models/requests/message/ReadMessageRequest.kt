package adapters.primaryweb.models.requests.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadMessageRequest(
    @SerialName("messageId") val messageId: Long
)
