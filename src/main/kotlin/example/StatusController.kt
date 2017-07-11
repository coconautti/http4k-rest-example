package example

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind

object StatusController {

    fun routes(): RoutingHttpHandler {

        val ping: HttpHandler = {
            Response(Status.OK).body("ok")
        }

        return org.http4k.routing.routes(
                "/ping" to Method.GET bind ping
        )
    }
}
