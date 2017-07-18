package example.user

class UserExistsException(message: String) : RuntimeException(message)

class RegistrationService(val repository: UserRepository) {

    fun register(user: User) {
        if (repository.exists(user.email)) {
            throw UserExistsException("User with email ${user.email} already exists")
        }

        repository.create(user)
    }
}
