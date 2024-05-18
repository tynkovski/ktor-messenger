package adapters.primaryweb.models.responses.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("id") val id: Long,
    @SerialName("login") val login: String,
    @SerialName("name") val name: String?,
    @SerialName("image") val image: String?,
    @SerialName("createdAt") val createdAt: String,
    @SerialName("isDeleted") val isDeleted: Boolean,
)
