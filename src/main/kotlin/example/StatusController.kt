package example

import org.http4k.core.*
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import org.http4k.format.Jackson.auto

data class Ping(val status: String)

object StatusController {
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
