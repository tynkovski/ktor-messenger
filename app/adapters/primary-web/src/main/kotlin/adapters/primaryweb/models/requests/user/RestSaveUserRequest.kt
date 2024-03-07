package adapters.primaryweb.models.requests.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request to add/update a user
 *
 * @param login
 * @param name
 * @param password
 */
@Serializable
data class RestSaveUserRequest (
    @SerialName(value = "name")
    val name: kotlin.String,

    @SerialName(value = "login")
    val login: kotlin.String,

    @SerialName(value = "password")
    val password: kotlin.String,
)
