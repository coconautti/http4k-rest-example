package example.user

import com.nhaarman.mockito_kotlin.*
import io.kotlintest.matchers.shouldThrow
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.on
import java.util.*

object RegistrationServiceSpec : Spek({
    given("a registration service") {
        on("register") {
            val repository = mock<UserRepository>()
            whenever(repository.exists("donald@disney.com")).doReturn(false)

            val service = RegistrationService(repository)

            val user = User(UUID.randomUUID().toString(), "donald@disney.com", "Donald", "Duck")
            service.register(user)
            verify(repository).exists(eq("donald@disney.com"))
            verify(repository).create(user)
        }

        on("register with existing email") {
            val repository = mock<UserRepository>()
            whenever(repository.exists(eq("donald@disney.com"))).doReturn(true)

            val service = RegistrationService(repository)

            val user = User(UUID.randomUUID().toString(), "donald@disney.com", "Donald", "Duck")
            shouldThrow<UserExistsException> {
                service.register(user)
            }
            verify(repository).exists(eq("donald@disney.com"))
            verify(repository, never()).create(any())
        }
    }
})
