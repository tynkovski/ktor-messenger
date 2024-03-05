package models

import core.models.UserEntry
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicLong

data class WebSocketMember(
    val session: WebSocketSession,
    val user: UserEntry
)