package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.message.DeleteMessageRequest
import adapters.primaryweb.models.requests.message.EditMessageRequest
import adapters.primaryweb.models.requests.message.ReadMessageRequest
import adapters.primaryweb.models.requests.message.SendMessageRequest
import adapters.primaryweb.models.responses.message.DeleteMessageResponse
import adapters.primaryweb.util.longParameter
import core.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

sealed class MessageControllerEvent(
    override val name: String
) : BaseControllerEvent {
    companion object {
        inline fun <reified R : Any> fromRequest(request: String): MessageControllerEvent {
            return when (val json = Json.decodeFromString<R>(request)) {
                is SendMessageRequest -> SendMessage(json)
                is EditMessageRequest -> EditMessage(json)
                is DeleteMessageRequest -> DeleteMessage(json)
                is ReadMessageRequest -> ReadMessage(json)
                else -> Unknown
            }
        }

        const val SEND_MESSAGE = "send_message"
        const val EDIT_MESSAGE = "edit_message"
        const val DELETE_MESSAGE = "delete_message"
        const val READ_MESSAGE = "read_message"
        const val UNKNOWN = "unknown"
    }

    data class SendMessage(
        val request: SendMessageRequest
    ) : MessageControllerEvent(SEND_MESSAGE)

    data class EditMessage(
        val request: EditMessageRequest
    ) : MessageControllerEvent(EDIT_MESSAGE)

    data class DeleteMessage(
        val request: DeleteMessageRequest
    ) : MessageControllerEvent(DELETE_MESSAGE)

    data class ReadMessage(
        val request: ReadMessageRequest
    ) : MessageControllerEvent(READ_MESSAGE)

    data object Unknown : MessageControllerEvent(UNKNOWN)
}

internal class MessageController(
    private val getMessageUsecase: GetMessageUsecase,
    private val getMessagesPagingUsecase: GetMessagesPagingUsecase,
    private val getMessageCountUsecase: GetMessageCountUsecase,
    private val sendMessageUsecase: SendMessageUsecase,
    private val deleteMessageUsecase: DeleteMessageUsecase,
    private val editMessageUsecase: EditMessageUsecase,
    private val readMessageUsecase: ReadMessageUsecase,
    private val getRoomUsecase: GetRoomUsecase,
) : BaseWebsocketsController<MessageControllerEvent>() {
    suspend fun getMessagesPaged(call: ApplicationCall) {
        val roomId = call.longParameter("roomId")
        val page = call.parameters["page"]?.toLong() ?: 0
        val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
        val count = getMessageCountUsecase.getMessageCount(roomId)
        val messages = getMessagesPagingUsecase.getMessages(roomId, page, pageSize)
        call.respond(status = HttpStatusCode.OK, message = messages.toResponse(count))
    }

    suspend fun getMessage(call: ApplicationCall) {
        val messageId = call.longParameter("id")
        val message = getMessageUsecase.getMessage(messageId)
        call.respond(status = HttpStatusCode.OK, message = message.toResponse())
    }

    private suspend fun getRoomUsers(roomId: Long): Set< Long> {
        return getRoomUsecase.getRoom(roomId).users.toSet()
    }

    private suspend fun getMessageUsers(messageId: Long): Set< Long> {
        val roomId = getMessageUsecase.getMessage(messageId).roomId
        return getRoomUsers(roomId)
    }

    override suspend fun processEvent(applicantId: Long, event: MessageControllerEvent) {
        when (event) {
            is MessageControllerEvent.SendMessage -> with(event.request) {
                val message = sendMessageUsecase.sendMessage(applicantId, roomId, text)
                val users = getRoomUsers(message.roomId)
                notifyUsersIfConnected(users, event.name, message.toResponse())
            }

            is MessageControllerEvent.EditMessage -> with(event.request) {
                val message = editMessageUsecase.editMessage(messageId, text)
                val users = getRoomUsers(message.roomId)
                notifyUsersIfConnected(users, event.name, message.toResponse())
            }

            is MessageControllerEvent.DeleteMessage -> with(event.request) {
                val users = getMessageUsers(messageId)
                deleteMessageUsecase.deleteMessage(messageId)
                notifyUsersIfConnected(users, event.name, DeleteMessageResponse(messageId))
            }

            is MessageControllerEvent.ReadMessage ->  with(event.request) {
                val message = readMessageUsecase.readMessage(applicantId, messageId)
                val users = getMessageUsers(messageId)
                notifyUsersIfConnected(users, event.name, message.toResponse())
            }

            MessageControllerEvent.Unknown -> Unit
        }
    }

    override suspend fun processText(event: String, json: String): MessageControllerEvent {
        return when (event) {
            MessageControllerEvent.SEND_MESSAGE ->
                MessageControllerEvent.fromRequest<SendMessageRequest>(json)

            MessageControllerEvent.EDIT_MESSAGE ->
                MessageControllerEvent.fromRequest<EditMessageRequest>(json)

            MessageControllerEvent.DELETE_MESSAGE ->
                MessageControllerEvent.fromRequest<DeleteMessageRequest>(json)

            MessageControllerEvent.READ_MESSAGE ->
                MessageControllerEvent.fromRequest<ReadMessageRequest>(json)

            else -> MessageControllerEvent.Unknown
        }
    }
}


