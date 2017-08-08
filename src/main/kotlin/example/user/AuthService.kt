package example.user

import com.lambdaworks.crypto.SCryptUtil
import org.joda.time.Duration

typealias AccessToken = String

data class SignUpAction(val firstName: String, val lastName: String, val email: String, val password: String)
data class LoginAction(val email: String, val password: String)
data class UserInfo(val id: UserId, val firstName: String, val lastName: String, val email: String)

interface AuthService {
    fun signUp(action: SignUpAction): AccessToken?
    fun login(action: LoginAction): AccessToken?
    fun authorize(token: AccessToken): UserId?
    fun me(id: UserId): UserInfo?
}

class TokenAuthService(private val userRepository: UserRepository, private val jwt: JWT) : AuthService {
    private val expiresIn = Duration.standardDays(1)

    override fun signUp(action: SignUpAction): AccessToken? {
        val (firstName, lastName, email, password) = action
        if (userRepository.exists(email)) {
            return null
        }

        val hashedPassword = SCryptUtil.scrypt(password, 16384, 8, 1)
        val user = userRepository.create(firstName, lastName, email, hashedPassword)
        return jwt.create(user.email, expiresIn)
    }

    override fun login(action: LoginAction): AccessToken? {
        val user = userRepository.findByEmail(action.email) ?: return null
        if (!SCryptUtil.check(action.password, user.password)) {
            return null
        }

        return jwt.create(user.email, expiresIn)
    }

    override fun authorize(token: AccessToken): UserId? {
        val email = jwt.verify(token) ?: return null
        return userRepository.findByEmail(email)?.id
    }

    override fun me(id: UserId): UserInfo? {
        val (_, firstName, lastName, email, _) = userRepository.findById(id) ?: return null
        return UserInfo(id, firstName, lastName, email)
    }
}
