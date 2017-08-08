package example.security

import io.kotlintest.matchers.fail
import io.kotlintest.matchers.shouldEqual
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.specs.FunSpec
import org.joda.time.Duration

class JavaJWTSpec : FunSpec() {
    val jwt: JWT = JavaJWT("secret", "example")

    init {
        test("create token") {
            jwt.create("peter@coconautti.com", Duration.standardDays(1)).shouldNotBe(null)
        }

        test("verify token") {
            jwt.create("peter@coconautti.com", Duration.standardDays(1))?.let { token ->
                jwt.verify(token).shouldEqual("peter@coconautti.com")
            } ?: fail("no token")
        }
    }
}
