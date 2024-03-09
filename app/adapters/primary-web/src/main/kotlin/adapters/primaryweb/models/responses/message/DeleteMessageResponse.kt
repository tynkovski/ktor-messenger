package adapters.primaryweb.models.responses.message

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageResponse(
    @SerialName("messageId") val messageId: Long
)
