package core.errors

class MemberExistsException(
    memberId: Long
) : DomainException(
    errorType = ERROR_TYPE,
    title = "Websocket member already exists",
    detail = "Websocket member already exists with id: $memberId",
    specifics = null
) {
    companion object {
        const val ERROR_TYPE = "/errors/member-exists"
    }
}