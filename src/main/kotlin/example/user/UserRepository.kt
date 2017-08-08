package example.user

import coconautti.sql.Database
import coconautti.sql.eq

class UserNotFoundException(message: String) : RuntimeException(message)

class UserRepository {

    fun create(firstName: String, lastName: String, email: String, password: String): User {
        val id = Database.insertInto("users") {
            columns("firstName", "lastName", "email", "password")
            values(firstName, lastName, email, password)
        }.execute() as UserId
        return User(id, firstName, lastName, email, password)
    }

    fun findById(id: UserId): User? {
        return Database.selectFrom("users") {
            columns("id", "firstName", "lastName", "email", "password")
            where("id" eq id)
        }.fetch<User>(User::class).firstOrNull()
    }

    fun findByEmail(email: String): User? {
        return Database.selectFrom("users") {
            columns("id", "firstName", "lastName", "email", "password")
            where("email" eq email)
        }.fetch<User>(User::class).firstOrNull()
    }

    fun exists(email: String): Boolean {
        return Database.selectFrom("users") {
            columns("id")
            where("email" eq email)
        }.query().firstOrNull() != null
    }
}
