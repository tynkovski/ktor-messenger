package adapters.primaryweb.models.requests.contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactsRequest(
    @SerialName("ids") val userIds: List<Long>
)
