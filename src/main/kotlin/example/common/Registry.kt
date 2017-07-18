package example.common

import example.user.RegistrationService
import example.user.UserRepository

object Registry {
    val userRepository = UserRepository()
    val registrationService = RegistrationService(userRepository)
}
