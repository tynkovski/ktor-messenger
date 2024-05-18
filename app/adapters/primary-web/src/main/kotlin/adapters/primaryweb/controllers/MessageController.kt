package adapters.primaryweb.controllers

import adapters.primaryweb.controllers.interfaces.UserPrincipalController
import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.responses.message.MessagesPagingResponse
import adapters.primaryweb.models.responses.message.UnreadResponse
import adapters.primaryweb.util.longParameter
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

internal class MessageController(
    private val getMessageUsecase: GetMessageUsecase,
    private val getMessagesPagingUsecase: GetMessagesPagingUsecase,
    private val getMessageCountUsecase: GetMessageCountUsecase,
) : UserPrincipalController {
    suspend fun getMessagesPaged(call: ApplicationCall) {
        val roomId = call.longParameter("roomId")
        val page = call.parameters["page"]?.toLong() ?: 0
        val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
        val count = getMessageCountUsecase.getMessageCount(roomId)
        val messages = getMessagesPagingUsecase.getMessages(roomId, page, pageSize)
        val response = MessagesPagingResponse(
            count = count,
            roomId = roomId,
            page = page,
            messages = messages.map { message -> message.toResponse() }
        )
        call.respond(status = HttpStatusCode.OK, message = response)
    }

    suspend fun getMessage(call: ApplicationCall) {
        val messageId = call.longParameter("id")
        val message = getMessageUsecase.getMessage(messageId)
        call.respond(status = HttpStatusCode.OK, message = message.toResponse())
    }
}


