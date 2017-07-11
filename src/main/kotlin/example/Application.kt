package example

import example.service.ServiceController
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("main")

    Database.connect("jdbc:h2:mem:example", "org.h2.Driver")

    val app = routes(
            "/service" bind ServiceController.routes()
    )

    app.asServer(Netty(8080)).start()

    log.info("Server ready at port 8080")
}
