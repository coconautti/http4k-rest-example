package example

import example.service.ServiceController
import example.user.UsersController
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.jetbrains.exposed.sql.Database
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("main")

    Database.connect("jdbc:h2:file:./example", "org.h2.Driver", "sa", "")

    val app = ServerFilters.CatchLensFailure.then(
            routes(
                "/service"      bind ServiceController.routes(),
                "/api/users"    bind UsersController.routes()
            )
    )

    app.asServer(Netty(8080)).start()

    log.info("Server ready at port 8080")
}
