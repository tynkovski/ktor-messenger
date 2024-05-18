package adapters.primaryweb.models.responses.message

import kotlinx.serialization.Serializable

@Serializable
data class UnreadResponse(
    val count: Int
)