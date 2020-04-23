package adapters.rest

import adapters.db.DatabaseErrorInspector
import adapters.db.DatabaseErrorState
import io.ktor.http.HttpStatusCode
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ports.provided.DomainException
import ports.provided.ResourceNotFoundException

data class RestErrorResponse(
    val type: String,
    val title: String,
    val status: Int,
    val detail: String,
    val instance: String?,
    val specifics: Map<String, Any?>? = null
)

fun DomainException.toRestErrorResponse(
    path: String
) = RestErrorResponse(
    type = errorType,
    title = title,
    status = guessHttpStatusCode().value,
    detail = detail,
    instance = path
)

private fun DomainException.guessHttpStatusCode(): HttpStatusCode =
    when (this) {
        is ResourceNotFoundException -> HttpStatusCode.NotFound
        else -> HttpStatusCode.InternalServerError
    }

fun ExposedSQLException.throwRestException(
    inspector: DatabaseErrorInspector
): Nothing {
    when (inspector.errorState(e = this)) {
        DatabaseErrorState.DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT ->
            throw RestDuplicateKeyValueException(sqlError = this.message)
        else ->
            throw RestSqlException(sqlError = this.message)
    }
}
