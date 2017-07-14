package example.user

class UserExistsException(message: String) : RuntimeException(message)

object RegistrationService {

    fun register(user: User) {
        if (UserRepository.exists(user.email)) {
            throw UserExistsException("User with email ${user.email} already exists")
        }

        UserRepository.create(user)
    }
}
