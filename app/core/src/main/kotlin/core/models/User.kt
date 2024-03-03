package core.models

import core.errors.ResourceNotFoundException
import java.time.LocalDateTime

data class UserEntry(
    val id: Long? = null,
    val name: String,
    val login: String,
    val password: String,
    val salt: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    companion object
}

class UserEntryNotFoundException(
    searchCriteria: String,
) : ResourceNotFoundException(
    title = "Requested user not found",
    detail = "User entry not found for search criteria: $searchCriteria",
    specifics = mapOf("searchCriteria" to searchCriteria)
)
