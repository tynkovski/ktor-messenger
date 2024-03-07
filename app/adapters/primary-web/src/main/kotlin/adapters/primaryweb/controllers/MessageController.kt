package adapters.primaryweb.controllers

import core.usecase.AddMessageUsecase

sealed class MessageControllerEvent : BaseControllerEvent {
    data object Unknown : MessageControllerEvent()
}

internal class MessageController(
    private val addMessageMessageUsecase: AddMessageUsecase
) : BaseWebsocketsController<MessageControllerEvent>() {
    override suspend fun processEvent(userId: Long, event: MessageControllerEvent) {
        // todo add logic
    }

    override suspend fun processText(event: String, json: String): MessageControllerEvent {
        // todo add logic
        return MessageControllerEvent.Unknown
    }
}