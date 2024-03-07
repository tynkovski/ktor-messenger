package adapters.primaryweb.mappers

import adapters.primaryweb.models.gen.RestGender
import adapters.primaryweb.models.gen.RestPersonResponse
import adapters.primaryweb.models.gen.RestPostalAddressResponse
import adapters.primaryweb.models.responses.RestTokenResponse
import adapters.primaryweb.models.responses.RestUserResponse
import adapters.primaryweb.models.responses.RoomResponse
import adapters.primaryweb.models.responses.RoomsPagingResponse
import core.models.PersonEntry
import core.models.RoomEntry
import core.models.TokenEntry
import core.models.UserEntry
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
        },
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

internal fun RoomEntry.toResponse(): RoomResponse = with(this) {
    RoomResponse(
        id = id!!,
        name = name,
        image = image,
        users = users.toList(),
        moderators = moderators.toList(),
        createdAt = formatter.format(createdAt)
    )
}

internal fun Collection<RoomEntry>.toResponse(count: Long): RoomsPagingResponse = with(this) {
    RoomsPagingResponse(
        count = count,
        rooms = map { room ->
            RoomResponse(
                id = room.id!!,
                name = room.name,
                image = room.image,
                users = room.users.toList(),
                moderators = room.moderators.toList(),
                createdAt = formatter.format(room.createdAt)
            )
        }
    )
}