package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class KeyStoreEntry(
    val id: Long? = null,
    val userId: Long,
    val accessKey: String,
    val refreshKey: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object
}

class KeyStoreEntryNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Key entry not found",
    detail = "Key entry not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)
