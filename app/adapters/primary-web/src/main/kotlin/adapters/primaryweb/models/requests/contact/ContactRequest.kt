package adapters.primaryweb.models.requests.contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactRequest(
    @SerialName("userId") val userId: Long
)
