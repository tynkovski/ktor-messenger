package adapters.primaryweb.models.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WebsocketsRequestWrapper<T : Any>(
    @SerialName("method") val method: String,
    @SerialName("body") val data: T,
)