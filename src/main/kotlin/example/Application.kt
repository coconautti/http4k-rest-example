package example

import coconautti.sql.Database
import example.common.ServerConfig
import example.service.ServiceController
import example.user.AuthController
import org.http4k.core.then
import org.http4k.filter.ServerFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    val log = LoggerFactory.getLogger("main")

    Database.connect(ServerConfig.dbUrl, ServerConfig.dbUsername, ServerConfig.dbPassword)

    val app = ServerFilters.CatchLensFailure.then(
            routes(
                "/service"  bind ServiceController.routes(),
                "/api/auth" bind AuthController.routes()
            )
    )

    val port = ServerConfig.port
    app.asServer(Netty(port)).start()

    log.info("Server ready at port $port")
}
