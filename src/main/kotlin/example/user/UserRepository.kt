package example.user

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserNotFoundException(message: String) : RuntimeException(message)

class UserRepository {
    object Users : Table() {
        val id = varchar("id", 36).primaryKey()
        val email = varchar("email", 128)
        val firstName = varchar("firstName", 32)
        val lastName = varchar("lastName", 64)
    }

    fun create(user: User) {
        transaction {
            Users.insert {
                it[id] = user.id
                it[email] = user.email
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }
        }
    }

    fun update(user: User) {
        transaction {
            Users.update({ Users.id.eq(user.id) }) {
                it[firstName] = user.firstName
                it[lastName] = user.lastName
            }
        }
    }

    fun remove(id: String) {
        transaction {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }

    fun findById(id: String): User {
        return transaction {
            val row = Users
                    .select { Users.id.eq(id) }
                    .firstOrNull()
            row ?: throw UserNotFoundException("User not found")
            User(row[Users.id], row[Users.email], row[Users.firstName], row[Users.lastName])
        }
    }

    fun exists(email: String): Boolean {
        return transaction {
            Users
                    .select { Users.email.eq(email) }
                    .count() > 0
        }
    }
}
