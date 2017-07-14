package example.user

import example.common.Error
import org.http4k.core.*
import org.http4k.format.Jackson.auto
import org.http4k.lens.Path
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import java.util.*

data class RegisterRequest(val email: String, val firstName: String, val lastName: String)
data class UpdateRequest(val firstName: String?, val lastName: String?)

object UsersController {
    val pathId = Path.of("id")
    val registerRequest = Body.auto<RegisterRequest>().toLens()
    val updateRequest = Body.auto<UpdateRequest>().toLens()

    val userLens = Body.auto<User>().toLens()
    val errorLens = Body.auto<Error>().toLens()

    fun routes(): RoutingHttpHandler {

        val register: HttpHandler = { request ->
            val req = registerRequest.extract(request)

            if (UserRepository.exists(req.email)) {
                val error = Error(code = 409, message = "user with email address ${req.email } already exists")
                errorLens.inject(error, Response(Status.CONFLICT))
            } else {
                val user = User(UUID.randomUUID().toString(), req.email, req.firstName, req.lastName)
                UserRepository.create(user)
                userLens.inject(user, Response(Status.OK))
            }
        }

        val findById: HttpHandler = { request ->
            val id = pathId(request)
            val user = UserRepository.findById(id)
            if (user != null) {
                userLens.inject(user, Response(Status.OK))
            } else {
                val error = Error(code = 404, message = "no such user")
                errorLens.inject(error, Response(Status.NOT_FOUND))
            }
        }

        val update: HttpHandler = { request ->
            val id = pathId(request)
            val req = updateRequest(request)

            val user = UserRepository.findById(id)
            if (user != null) {
                if (req.firstName != null) user.firstName = req.firstName
                if (req.lastName != null) user.lastName = req.lastName
                UserRepository.update(user)
                userLens.inject(user, Response(Status.OK))
            } else {
                val error = Error(code = 404, message = "no such user")
                errorLens.inject(error, Response(Status.NOT_FOUND))
            }

        }

        return org.http4k.routing.routes(
                "/"     to Method.POST  bind register,
                "/{id}" to Method.GET   bind findById,
                "/{id}" to Method.PUT   bind update
        )
    }
}
