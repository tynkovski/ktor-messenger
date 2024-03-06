package adapters.primaryweb.controllers

import core.usecase.AddMessageUsecase
import io.ktor.server.websocket.*
import io.ktor.websocket.*

internal class MessageController(
    private val addMessageMessageUsecase: AddMessageUsecase
) : BaseWebsocketsController() {
    override suspend fun processFrame(session: DefaultWebSocketServerSession, frame: Frame) {

    }
}