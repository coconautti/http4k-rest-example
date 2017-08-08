package example.user

import com.lambdaworks.crypto.SCryptUtil
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.whenever
import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.mock.mock
import io.kotlintest.specs.FunSpec

class TokenAuthServiceSpec : FunSpec() {
    val jwt: JWT = JavaJWT("secret", "test")

    init {

        test("sign up") {
            val userRepository = mock<UserRepository>()
            whenever(userRepository.exists(eq("peter@coconautti.com"))).thenReturn(false)
            whenever(userRepository.create(eq("Peter"), eq("Hägg"), eq("peter@coconautti.com"), any()))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", ""))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = SignUpAction("Peter", "Hägg", "peter@coconautti.com", "password")
            authService.signUp(action).shouldNotBe(null)
        }

        test("sign up fails w/ existing email") {
            val userRepository = mock<UserRepository>()
            whenever(userRepository.exists(eq("peter@coconautti.com"))).thenReturn(true)
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = SignUpAction("Peter", "Hägg", "peter@coconautti.com", "password")
            authService.signUp(action).shouldBe(null)
        }

        test("log in") {
            val userRepository = mock<UserRepository>()
            val hashedPassword = SCryptUtil.scrypt("password", 16384, 8, 1)
            whenever(userRepository.findByEmail(eq("peter@coconautti.com")))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", hashedPassword))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = LoginAction("peter@coconautti.com", "password")
            authService.login(action).shouldNotBe(null)
        }

        test("log fails w/ non-existent user") {
            val userRepository = mock<UserRepository>()
            whenever(userRepository.findByEmail(eq("peter@coconautti.com"))).thenReturn(null)
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = LoginAction("peter@coconautti.com", "password")
            authService.login(action).shouldBe(null)
        }

        test("log in fails w/ invalid password") {
            val userRepository = mock<UserRepository>()
            val hashedPassword = SCryptUtil.scrypt("password", 16384, 8, 1)
            whenever(userRepository.findByEmail(eq("peter@coconautti.com")))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", hashedPassword))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = LoginAction("peter@coconautti.com", "invalid")
            authService.login(action).shouldBe(null)
        }

        test("authorize") {
            val userRepository = mock<UserRepository>()
            val hashedPassword = SCryptUtil.scrypt("password", 16384, 8, 1)
            whenever(userRepository.findByEmail(eq("peter@coconautti.com")))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", hashedPassword))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = LoginAction("peter@coconautti.com", "password")
            authService.login(action)?.let { token ->
                authService.authorize(token).shouldNotBe(null)
            } ?: fail("test failure")
        }

        test("authorization fails w/ invalid token") {
            val userRepository = UserRepository()
            val authService: AuthService = TokenAuthService(userRepository, jwt)
            authService.authorize("invalid").shouldBe(null)
        }

        test("authorization fails w/ missing user") {
            val userRepository = mock<UserRepository>()
            val hashedPassword = SCryptUtil.scrypt("password", 16384, 8, 1)
            whenever(userRepository.findByEmail(eq("peter@coconautti.com")))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", hashedPassword))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            val action = LoginAction("peter@coconautti.com", "password")
            authService.login(action)?.let { token ->
                whenever(userRepository.findByEmail(eq("peter@coconautti.com"))).thenReturn(null)
                authService.authorize(token).shouldBe(null)
            } ?: fail("test failure")
        }

        test("me") {
            val userRepository = mock<UserRepository>()
            whenever(userRepository.findById(eq(1)))
                    .thenReturn(User(1, "Peter", "Hägg", "peter@coconautti.com", ""))
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            authService.me(1)?.let { (id, firstName, lastName, email) ->
                id.shouldEqual(1L)
                firstName.shouldEqual("Peter")
                lastName.shouldEqual("Hägg")
                email.shouldEqual("peter@coconautti.com")
            } ?: fail("test failure")
        }

        test("me fails w/ user not found") {
            val userRepository = mock<UserRepository>()
            whenever(userRepository.findById(eq(1))).thenReturn(null)
            val authService: AuthService = TokenAuthService(userRepository, jwt)

            authService.me(1).shouldBe(null)
        }
    }
}
