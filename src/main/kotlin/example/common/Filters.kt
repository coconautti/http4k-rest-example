package example.common

import example.user.UserId
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request

val Request.userId: UserId?
    get() = this.header("x-user-id")?.toLong()

object Filters {
    object AuthFilter : Filter {
        override fun invoke(next: HttpHandler): HttpHandler = {
            it.header("Authorization")?.let { token ->
                Registry.authService.authorize(token)?.let { id ->
                    next(it.header("x-user-id", id.toString()))
                }
            } ?: Responses.UNAUTHORIZED
        }
    }
}
