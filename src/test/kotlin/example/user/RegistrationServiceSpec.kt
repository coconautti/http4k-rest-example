package example.user

import com.nhaarman.mockito_kotlin.*
import io.kotlintest.matchers.shouldThrow
import io.kotlintest.specs.FunSpec
import java.util.*

class RegistrationServiceSpec : FunSpec({

    test("register user") {
        val repository = mock<UserRepository>()
        whenever(repository.exists("donald@disney.com")).doReturn(false)

        val service = RegistrationService(repository)

        val user = User(UUID.randomUUID().toString(), "donald@disney.com", "Donald", "Duck")
        service.register(user)
        verify(repository).exists(eq("donald@disney.com"))
        verify(repository).create(user)
    }

    test("register with existing email") {
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
})
