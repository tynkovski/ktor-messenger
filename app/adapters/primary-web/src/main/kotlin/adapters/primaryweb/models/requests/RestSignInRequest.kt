package adapters.primaryweb.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param login
 * @param password
 */
@Serializable
data class RestSignInRequest (
    @SerialName(value = "login")
    val login: kotlin.String,

    @SerialName(value = "password")
    val password: kotlin.String,
)
