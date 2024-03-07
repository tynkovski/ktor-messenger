package adapters.primaryweb.util

import kotlinx.serialization.json.Json

inline fun <reified T : Any> String.ifCanDecodeAs(block: T.() -> Unit) {
    runCatching { Json.decodeFromString<T>(this).block() }
        .onFailure {
            if (it !is SecurityException && it !is IllegalArgumentException) {
                throw it
            }
        }
}