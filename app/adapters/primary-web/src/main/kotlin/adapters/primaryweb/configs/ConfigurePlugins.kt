package adapters.primaryweb.configs

import adapters.primaryweb.mappers.toRestGenericException
import adapters.primaryweb.util.RestGenericException
import adapters.primaryweb.util.RestInternalServerError
import adapters.primaryweb.util.respondRestException
import com.github.michaelbull.logging.InlineLogger
import common.log.X_REQUEST_ID_LOG_KEY
import common.log.setXRequestId
import core.errors.DomainException
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import org.slf4j.event.Level
import java.time.Duration
import java.util.*

private val logger = InlineLogger()

internal fun Application.configurePlugins() {
    install(ContentNegotiation) { json() }

    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
        mdc(X_REQUEST_ID_LOG_KEY) { call ->
            call.response.headers[HttpHeaders.XRequestId]
        }
    }

    install(CallId) {
        generate {
            val requestId = it.request.header(HttpHeaders.XRequestId)
            if (requestId.isNullOrEmpty()) {
                "${UUID.randomUUID()}"
            } else {
                requestId
            }
        }
        replyToHeader(HttpHeaders.XRequestId)
    }

    install(CORS) {
        anyHost()
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
    }

    // Return proper HTTP error: https://ktor.io/features/status-pages.html
    // In this block we are mapping Domain and Adapter exceptions into proper HTTP error response.
    install(StatusPages) {
        exception<Exception> { call, e ->
            setXRequestId(call.callId)
            logger.error(e) { "StatusPages/exception(): Error to be returned to a caller" }
            when (e) {
                is DomainException -> {
                    val errorResponse = e.toRestGenericException().toRestErrorResponse(
                        path = call.request.uri,
                    )
                    call.respond(
                        status = HttpStatusCode.fromValue(errorResponse.status),
                        message = errorResponse,
                    )
                }

                is RestGenericException -> {
                    call.respondRestException(e)
                }

                else -> {
                    call.respondRestException(
                        RestInternalServerError(detail = e.message ?: e.toString()),
                    )
                }
            }
        }
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
}