package example.service

import org.http4k.core.Body
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Status
import org.http4k.core.Response
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.format.Jackson.auto

data class Ping(val status: String)

object ServiceController {
    val pingLens = Body.auto<Ping>().toLens()

    fun routes(): RoutingHttpHandler {

        val ping: HttpHandler = {
            pingLens.inject(Ping("ok"), Response(Status.OK))
        }

        return org.http4k.routing.routes(
                "/ping" to Method.GET bind ping
        )
    }
}
