package adapters.primaryweb.mappers

import adapters.primaryweb.models.gen.RestGender
import adapters.primaryweb.models.gen.RestPersonResponse
import adapters.primaryweb.models.gen.RestPostalAddressResponse
import adapters.primaryweb.models.responses.auth.RestTokenResponse
import adapters.primaryweb.models.responses.message.MessageResponse
import adapters.primaryweb.models.responses.message.MessagesPagingResponse
import adapters.primaryweb.models.responses.room.RoomLastActionResponse
import adapters.primaryweb.models.responses.room.RoomResponse
import adapters.primaryweb.models.responses.room.RoomsPagingResponse
import adapters.primaryweb.models.responses.user.RestUserResponse
import core.models.*
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
private val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

internal fun PersonEntry.toResponse(): RestPersonResponse = with(this) {
    RestPersonResponse(
        id = id!!,
        firstName = firstName,
        lastName = lastName,
        gender = gender?.toResponse(),
        age = age,
        phoneNumber = phoneNumber,
        email = email,
        postalAddress = postalAddress?.let {
            RestPostalAddressResponse(
                address1 = it.address1,
                address2 = it.address2,
                city = it.city,
                state = it.state,
                country = it.country,
            )
        }
    )
}

internal fun PersonEntry.Gender.toResponse(): RestGender = when (this) {
    PersonEntry.Gender.MALE -> RestGender.male
    PersonEntry.Gender.FEMALE -> RestGender.female
}

internal fun UserEntry.toResponse(): RestUserResponse = with(this) {
    RestUserResponse(
        id = id!!,
        name = name,
        login = login,
        createdAt = formatter.format(createdAt)
    )
}

internal fun TokenEntry.toResponse(): RestTokenResponse = with(this) {
    RestTokenResponse(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

internal fun RoomEntry.LastActionEntry.toResponse() = with(this) {
    RoomLastActionResponse(
        authorId = applicantId,
        actionType = actionType.toString(),
        description = description,
        actionDateTime = formatter.format(actionDateTime)
    )
}

internal fun RoomEntry.toResponse(): RoomResponse = with(this) {
    RoomResponse(
        id = id!!,
        name = name,
        image = image,
        users = users.toList(),
        moderators = moderators.toList(),
        lastAction = lastAction.toResponse(),
        createdAt = formatter.format(createdAt),
    )
}

internal fun Collection<RoomEntry>.toResponse(count: Long): RoomsPagingResponse = with(this) {
    RoomsPagingResponse(
        count = count,
        rooms = map { room -> room.toResponse() }
    )
}

internal fun MessageEntry.toResponse(): MessageResponse = with(this) {
    MessageResponse(
        id = id!!,
        senderId = senderId,
        roomId = roomId,
        text = text,
        readBy = readBy.toList(),
        editedAt = editedAt?.let { formatter.format(it) },
        sentAt = formatter.format(sentAt)
    )
}

internal fun Collection<MessageEntry>.toResponse(count: Long): MessagesPagingResponse = with(this) {
    MessagesPagingResponse(
        count = count,
        messages = map { message -> message.toResponse() }
    )
}

