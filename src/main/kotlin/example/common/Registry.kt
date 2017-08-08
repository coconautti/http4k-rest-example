package example.common

import example.security.JWT
import example.security.JavaJWT
import example.user.RegistrationService
import example.user.UserRepository

object Registry {
    val jwt: JWT = JavaJWT(ServerConfig.jwtSecret, "example")

    val userRepository = UserRepository()
    val registrationService = RegistrationService(userRepository)
}
