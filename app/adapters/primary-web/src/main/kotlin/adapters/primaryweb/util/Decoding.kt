package adapters.primaryweb.util

import com.github.michaelbull.logging.InlineLogger
import kotlinx.serialization.json.Json

inline fun <reified T : Any> String.ifCanDecodeAs(block: T.() -> Unit) {
    runCatching { Json.decodeFromString<T>(this).block() }
        .onFailure {
            if (it !is SecurityException && it !is IllegalArgumentException) {
                val logger = InlineLogger()
                logger.error(it) { "ifCanDecodeAs(): Bad request ${T::class.simpleName}" }
                throw it
            }
        }
}