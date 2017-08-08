package example.common

import org.http4k.core.Response
import org.http4k.core.Status

data class Error(val error: Boolean = true, val code: Int, val message: String? = null)

object Responses {
    val BAD_REQUEST = Response(Status.BAD_REQUEST)
    val CONFLICT = Response(Status.CONFLICT)
    val FORBIDDEN = Response(Status.FORBIDDEN)
    val INTERNAL_SERVER_ERROR = Response(Status.INTERNAL_SERVER_ERROR)
    val OK = Response(Status.OK)
    val UNAUTHORIZED = Response(Status.UNAUTHORIZED)
}
