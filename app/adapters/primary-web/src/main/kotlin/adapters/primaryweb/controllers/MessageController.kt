package adapters.primaryweb.controllers

import core.usecase.AddMessageUsecase

sealed class MessageControllerEvent(
    override val name: String
) : BaseControllerEvent {
    companion object {
        const val UNKNOWN = "unknown"
    }
    data object Unknown : MessageControllerEvent(UNKNOWN)
}

internal class MessageController(
    private val addMessageMessageUsecase: AddMessageUsecase
) : BaseWebsocketsController<MessageControllerEvent>() {
    override suspend fun processEvent(applicantId: Long, event: MessageControllerEvent) {
        // todo add logic
    }

    override suspend fun processText(event: String, json: String): MessageControllerEvent {
        // todo add logic
        return MessageControllerEvent.Unknown
    }
}