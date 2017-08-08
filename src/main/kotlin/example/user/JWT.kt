package example.user

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

interface JWT {
    fun create(subject: String, expiresIn: Duration): String?
    fun verify(token: String): String?
}

class JavaJWT(private val secret: String, val issuer: String) : JWT {
    private val log = LoggerFactory.getLogger(JavaJWT::class.java)

    override fun create(subject: String, expiresIn: Duration): String? {
        try {
            val algorithm = Algorithm.HMAC256(secret)
            return com.auth0.jwt.JWT
                    .create()
                    .withIssuer(issuer)
                    .withSubject(subject)
                    .withExpiresAt(DateTime.now().plus(expiresIn).toDate())
                    .sign(algorithm)
        } catch (e: JWTCreationException) {
            log.error("Unable to create JWT token", e)
            return null
        }
    }

    override fun verify(token: String): String? {
        try {
            val decodedToken = verifier.verify(token)
            return decodedToken.subject
        } catch (e: JWTVerificationException) {
            log.info("Unable to verify JWT token", e)
            return null
        }
    }

    private val verifier by lazy {
        val algorithm = Algorithm.HMAC256(secret)
        com.auth0.jwt.JWT
                .require(algorithm)
                .acceptExpiresAt(60)
                .build()
    }
}
