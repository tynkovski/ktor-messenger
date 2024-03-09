package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.util.longParameter
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

sealed class MessageControllerEvent(
    override val name: String
) : BaseControllerEvent {
    companion object {
        const val UNKNOWN = "unknown"
    }

    data object Unknown : MessageControllerEvent(UNKNOWN)
}

internal class MessageController(
    private val getMessageUsecase: GetMessageUsecase,
    private val getMessagesPagingUsecase: GetMessagesPagingUsecase,
    private val getMessageCountUsecase: GetMessageCountUsecase,
    private val addMessageMessageUsecase: AddMessageUsecase,
    private val deleteMessageUsecase: DeleteMessageUsecase,
    private val editMessageUsecase: EditMessageUsecase,
    private val readMessageUsecase: ReadMessageUsecase,
) : BaseWebsocketsController<MessageControllerEvent>() {
    suspend fun getMessagesPaged(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val roomId = call.longParameter("roomId")
        val page = call.parameters["page"]?.toLong()!!
        val pageSize = call.parameters["pageSize"]?.toInt()!!
        val count = getMessageCountUsecase.getMessageCount(roomId)
        val messages = getMessagesPagingUsecase.getMessages(roomId, page, pageSize)
        call.respond(status = HttpStatusCode.OK, message = messages.toResponse(count))

    }

    suspend fun getMessage(call: ApplicationCall) {
        val userId = findUser(call).id!!
        val messageId = call.longParameter("id")
        val message = getMessageUsecase.getMessage(messageId)
        call.respond(status = HttpStatusCode.OK, message = message.toResponse())
    }


    override suspend fun processEvent(applicantId: Long, event: MessageControllerEvent) {
        // todo add logic
    }

    override suspend fun processText(event: String, json: String): MessageControllerEvent {
        // todo add logic
        return MessageControllerEvent.Unknown
    }
}


