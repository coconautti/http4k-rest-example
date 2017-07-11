package example

import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main(args: Array<String>) {
    val app = routes(
            "/service" bind StatusController.routes()
    )

    app.asServer(Netty(8080)).start()

    print("Service ready at port 8080")
}
