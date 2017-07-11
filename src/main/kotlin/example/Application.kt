package example

import example.service.ServiceController
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.jetbrains.exposed.sql.Database

fun main(args: Array<String>) {
    Database.connect("jdbc:h2:mem:example", "org.h2.Driver")

    val app = routes(
            "/service" bind ServiceController.routes()
    )

    app.asServer(Netty(8080)).start()

    print("Service ready at port 8080")
}
