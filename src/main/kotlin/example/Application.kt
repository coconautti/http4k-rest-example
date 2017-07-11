package example

import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val app = routes(
            "/ping" to Method.GET bind { Response(Status.OK).body("ok") }
    )
    app.asServer(Netty(8080)).start()
    print("Service ready at port 8080")
}
