package adapters.primaryweb.controllers

import adapters.primaryweb.mappers.toResponse
import adapters.primaryweb.models.requests.message.DeleteMessageRequest
import adapters.primaryweb.models.requests.message.EditMessageRequest
import adapters.primaryweb.models.requests.message.ReadMessageRequest
import adapters.primaryweb.models.requests.message.SendMessageRequest
import adapters.primaryweb.models.requests.room.*
import core.models.RoomEntry
import core.usecase.*
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

const val CREATE_ROOM = "create_room"
const val UPDATE_ROOM = "update_room"
const val RENAME_ROOM = "rename_room"
const val DELETE_ROOM = "delete_room"
const val INVITE_USER_TO_ROOM = "invite_user_to_room"
const val KICK_USER_FROM_ROOM = "kick_user_from_room"
const val JOIN_TO_ROOM = "join_to_room"
const val QUIT_FROM_ROOM = "quit_from_room"
const val MAKE_MODERATOR = "make_moderator"

const val SEND_MESSAGE = "send_message"
const val EDIT_MESSAGE = "edit_message"
const val DELETE_MESSAGE = "delete_message"
const val READ_MESSAGE = "read_message"

const val UNKNOWN = "unknown"

class RealtimeChatController(
    private val getMessageUsecase: GetMessageUsecase,
    private val getUserUsecase: GetUserUsecase,
    private val getRoomUsecase: GetRoomUsecase,
    private val updateRoomUsecase: UpdateRoomUsecase,
    private val sendMessageUsecase: SendMessageUsecase,
    private val deleteMessageUsecase: DeleteMessageUsecase,
    private val editMessageUsecase: EditMessageUsecase,
    private val readMessageUsecase: ReadMessageUsecase,
    private val createRoomUsecase: CreateRoomUsecase,
    private val renameRoomUsecase: RenameRoomUsecase,
    private val joinToRoomUsecase: JoinToRoomUsecase,
    private val quitFromRoomUsecase: QuitFromRoomUsecase,
    private val inviteUserToRoomUsecase: InviteUserToRoomUsecase,
    private val kickUserFromRoomUsecase: KickUserFromRoomUsecase,
    private val makeModeratorInRoomUsecase: MakeModeratorInRoomUsecase,
    private val deleteRoomUsecase: DeleteRoomUsecase,
) : BaseWebsocketsController() {
    private suspend fun getLastActionAuthor(room: RoomEntry): String? {
        val lastAction = room.lastAction ?: return null
        val authorId = lastAction.applicantId
        val user = getUserUsecase.getUser(authorId)
        return user.name ?: user.login
    }

    private suspend fun getRoom(roomId: Long): RoomEntry {
        return getRoomUsecase.getRoom(roomId)
    }

    private suspend fun getRoomOfMessage(messageId: Long): RoomEntry {
        val roomId = getMessageUsecase.getMessage(messageId).roomId
        return getRoom(roomId)
    }

    override suspend fun processEvent(applicantId: Long, event: String, json: String) {
        when (event) {
            CREATE_ROOM -> {
                val request = Json.decodeFromString<CreateRoomRequest>(json)
                with(request) {
                    val usersSet = users.toSet()
                    val room = createRoomUsecase.createRoom(
                        applicantId = applicantId,
                        name = name,
                        image = image,
                        users = usersSet,
                        moderators = usersSet
                    )
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(usersSet, event, room.toResponse(authorName))
                }
            }

            RENAME_ROOM -> {
                val request = Json.decodeFromString<RenameRoomRequest>(json)
                with(request) {
                    val room = renameRoomUsecase.renameRoom(applicantId, roomId, name)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            DELETE_ROOM -> {
                val request = Json.decodeFromString<DeleteRoomRequest>(json)
                with(request) {
                    val usersSet = getRoom(roomId).users
                    val room = deleteRoomUsecase.deleteRoom(roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(usersSet, event, room.toResponse(authorName))
                }
            }

            JOIN_TO_ROOM -> {
                val request = Json.decodeFromString<JoinToRoomRequest>(json)
                with(request) {
                    val room = joinToRoomUsecase.joinUser(applicantId, roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            QUIT_FROM_ROOM -> {
                val request = Json.decodeFromString<QuitFromRoomRequest>(json)
                with(request) {
                    val room = quitFromRoomUsecase.quitUser(applicantId, roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            INVITE_USER_TO_ROOM -> {
                val request = Json.decodeFromString<InviteUserToRoomRequest>(json)
                with(request) {
                    val room = inviteUserToRoomUsecase.inviteUser(applicantId, userId, roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            KICK_USER_FROM_ROOM -> {
                val request = Json.decodeFromString<KickUserFromRoomRequest>(json)
                with(request) {
                    val room = kickUserFromRoomUsecase.kickUser(applicantId, userId, roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            MAKE_MODERATOR -> {
                val request = Json.decodeFromString<MakeModeratorToRoomRequest>(json)
                with(request) {
                    val room = makeModeratorInRoomUsecase.makeModerator(applicantId, userId, roomId)
                    val authorName = getLastActionAuthor(room)
                    notifyUsersIfConnected(room.users, event, room.toResponse(authorName))
                }
            }

            SEND_MESSAGE -> {
                val request = Json.decodeFromString<SendMessageRequest>(json)
                with(request) {
                    val message = sendMessageUsecase.sendMessage(applicantId, roomId, text)
                    val users = getRoom(message.roomId).users
                    notifyUsersIfConnected(users, event, message.toResponse())

                    val room = getRoomUsecase.getRoom(roomId).copy(
                        lastAction = RoomEntry.LastActionEntry(
                            applicantId = applicantId,
                            actionType = RoomEntry.LastActionEntry.ActionType.USER_SENT_MESSAGE,
                            description = text,
                            actionDateTime = LocalDateTime.now()
                        )
                    )

                    val newRoom = updateRoomUsecase.updateRoom(room)
                    val authorName = getLastActionAuthor(newRoom)
                    notifyUsersIfConnected(users, UPDATE_ROOM, newRoom.toResponse(authorName))
                }
            }

            EDIT_MESSAGE -> {
                val request = Json.decodeFromString<EditMessageRequest>(json)
                with(request) {
                    val message = editMessageUsecase.editMessage(messageId, text)
                    val users = getRoom(message.roomId).users
                    notifyUsersIfConnected(users, event, message.toResponse())
                }
            }

            DELETE_MESSAGE -> {
                val request = Json.decodeFromString<DeleteMessageRequest>(json)
                with(request) {
                    val room = getRoomOfMessage(messageId)
                    val message = deleteMessageUsecase.deleteMessage(messageId)
                    notifyUsersIfConnected(room.users, event, message.toResponse())
                }
            }

            READ_MESSAGE -> {
                val request = Json.decodeFromString<ReadMessageRequest>(json)
                with(request) {
                    val message = readMessageUsecase.readMessage(applicantId, messageId)
                    val room = getRoomOfMessage(messageId)
                    notifyUsersIfConnected(room.users, event, message.toResponse())
                }
            }

            else -> {
                notifyUsersIfConnected(setOf(applicantId), UNKNOWN, "unknown event '$event'")
            }
        }
    }
}