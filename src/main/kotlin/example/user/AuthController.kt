package example.user

import example.common.Filters
import example.common.Registry
import example.common.Responses
import example.common.userId
import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.then
import org.http4k.routing.RoutingHttpHandler
import org.http4k.format.Jackson.auto
import org.http4k.routing.bind

data class AuthResponse(val token: String)

object AuthController {
    val authService = Registry.authService

    val signUpAction = Body.auto<SignUpAction>().toLens()
    val loginAction = Body.auto<LoginAction>().toLens()

    val authResponse = Body.auto<AuthResponse>().toLens()
    val userInfoResponse = Body.auto<UserInfo>().toLens()

    fun routes(): RoutingHttpHandler {

        val signUp: HttpHandler = { request ->
            authService.signUp(signUpAction(request))?.let { token ->
                authResponse.inject(AuthResponse(token), Responses.OK)
            } ?: Responses.BAD_REQUEST
        }

        val login: HttpHandler = { request ->
            authService.login(loginAction(request))?.let { token ->
                authResponse.inject(AuthResponse(token), Responses.OK)
            } ?: Responses.FORBIDDEN
        }

        val me: HttpHandler = Filters.AuthFilter.then({ request ->
            request.userId?.let { userId ->
                authService.me(userId)?.let { userInfo ->
                    userInfoResponse.inject(userInfo, Responses.OK)
                }
            } ?: Responses.UNAUTHORIZED
        })

        return org.http4k.routing.routes(
                "/signup"   bind Method.POST    to signUp,
                "/login"    bind Method.POST    to login,
                "/me"       bind Method.GET     to me
        )
    }
}