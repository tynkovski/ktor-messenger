package core.outport

import core.models.UserEntry

interface FindUserForAccessKeyPort {
    fun findUserForAccessKey(accessKey: String): UserEntry?
}