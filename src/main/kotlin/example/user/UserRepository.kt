package example.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserRepository {
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

    fun findById(id: String, handler: (User?) -> Any): Any {
        return transaction {
            val row = Users
                    .select { Users.id.eq(id) }
                    .firstOrNull()
            if (row != null) {
                handler(User(row[Users.id], row[Users.email], row[Users.firstName], row[Users.lastName]))
            } else {
                handler(null)
            }
        }
    }

    fun exists(email: String, handler: (Boolean) -> Any): Any {
        return transaction {
            handler(Users
                    .select { Users.email.eq(email) }
                    .count() > 0
            )
        }
    }
}
