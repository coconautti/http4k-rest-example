package example.common

import example.user.AuthService
import example.user.JWT
import example.user.JavaJWT
import example.user.TokenAuthService
import example.user.UserRepository

object Registry {

    val userRepository = UserRepository()

    val jwt: JWT = JavaJWT(ServerConfig.jwtSecret, "example")
    val authService: AuthService = TokenAuthService(userRepository, jwt)
}
