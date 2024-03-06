package adapters.primaryweb.models

import core.models.UserEntry
import io.ktor.websocket.*

data class WebSocketMember(
    val session: WebSocketSession,
    val user: UserEntry
)