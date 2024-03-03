package adapters.primaryweb.models.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestUserResponse(
    @SerialName(value = "id")
    val id: kotlin.Long,

    @SerialName(value = "name")
    val name: kotlin.String,

    @SerialName(value = "login")
    val login: kotlin.String,

    @SerialName(value = "createdAt")
    val createdAt: kotlin.String,
)
