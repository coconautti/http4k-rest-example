package example.user

import coconautti.sql.Database
import coconautti.sql.eq

class UserNotFoundException(message: String) : RuntimeException(message)

class UserRepository {

    fun create(user: User) {
        val (id, email, firstName, lastName) = user
        Database.insertInto("users") {
            columns("id", "email", "firstName", "lastName")
            values(id, email, firstName, lastName)
        }.execute()
    }

    fun update(user: User) {
        Database.update("users") {
            set("firstName", user.firstName)
            set("lastName", user.lastName)
        }.execute()
    }

    fun remove(id: String) {
        Database.deleteFrom("users") {
            where("id" eq  id)
        }
    }

    fun findById(id: String): User? {
        return Database.selectFrom("users") {
            columns("id", "email", "firstName", "lastName")
            where("id" eq id)
        }.fetch<User>(User::class).firstOrNull()
    }

    fun exists(email: String): Boolean {
        return Database.selectFrom("users") {
            columns("id")
            where("email" eq email)
        }.query().firstOrNull() != null
    }
}
