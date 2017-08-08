package example.user

typealias UserId = Long

data class User(val id: UserId, var firstName: String, var lastName: String, val email: String, val password: String)
