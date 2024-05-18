package adapters.primaryweb.models.responses.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContactsResponse(
    @SerialName("contacts") val usersId: List<Long>
)
