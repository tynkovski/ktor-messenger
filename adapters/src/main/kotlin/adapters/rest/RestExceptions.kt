package adapters.rest

import io.ktor.http.HttpStatusCode
import java.lang.RuntimeException

open class RestGenericException(
    private val type: String,
    private val title: String,
    val status: HttpStatusCode,
    private val detail: String,
    private val specifics: Map<String, Any?>? = null
) : RuntimeException() {
    fun toRestErrorResponse(path: String) = RestErrorResponse(
        type = type,
        title = title,
        status = status.value,
        detail = detail,
        instance = path,
        specifics = specifics
    )

    override fun toString() = "$this type=\"$type\", title=\"$title\", status=\"$status\", " +
        "detail=\"$detail\", specifics=$specifics"
}

data class RestMissingRequiredJsonFieldException(
    val fieldName: String,
    val fieldType: String?
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Missing required JSON field",
    status = HttpStatusCode.BadRequest,
    detail = "Validate JSON payload for mandatory fields",
    specifics = mapOf(
        "jsonField" to mapOf(
            "name" to fieldName,
            "type" to fieldType
        )
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/missing-required-json-field"
    }
}

data class RestInvalidFormatJsonFieldException(
    val fieldName: String,
    val fieldType: String?
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Invalid format of JSON field",
    status = HttpStatusCode.BadRequest,
    detail = "Validate JSON field has valid format",
    specifics = mapOf(
        "jsonField" to mapOf(
            "name" to fieldName,
            "type" to fieldType
        )
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/invalid-format-json-field"
    }
}

data class RestErrorParsingJsonException(
    val detail: String
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Error parsing JSON payload",
    status = HttpStatusCode.BadRequest,
    detail = detail
) {
    companion object {
        const val ERROR_TYPE = "/errors/failed-parsing-json"
    }
}

data class RestMissingRequestParameterException(
    val paramName: String
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Missing request parameter",
    status = HttpStatusCode.BadRequest,
    detail = "Parameter \"$paramName\" must be provided in request",
    specifics = mapOf(
        "paramName" to paramName
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/missing-request-parameter"
    }
}

data class RestInvalidRequestParameterFormatException(
    val paramName: String,
    val detail: String
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Invalid request parameter format",
    status = HttpStatusCode.BadRequest,
    detail = detail,
    specifics = mapOf(
        "paramName" to paramName
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/invalid-request-parameter-format"
    }
}

data class RestDuplicateKeyValueException(
    val sqlError: String?
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Duplicate key/value in entity",
    status = HttpStatusCode.BadRequest,
    detail = "Cannot store data because some fields are duplicated and conflicting with previously stored entries",
    specifics = mapOf(
        "sqlError" to sqlError
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/duplicate-key-value"
    }
}

data class RestSqlException(
    val sqlError: String?
) : RestGenericException(
    type = ERROR_TYPE,
    title = "Error performing SQL request",
    status = HttpStatusCode.InternalServerError,
    detail = "Something went wrong while executing database SQL request",
    specifics = mapOf(
        "sqlError" to sqlError
    )
) {
    companion object {
        const val ERROR_TYPE = "/errors/sql-error"
    }
}
