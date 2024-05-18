package adapters.primaryweb.models.responses.user

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val users: List<UserResponse>,
)