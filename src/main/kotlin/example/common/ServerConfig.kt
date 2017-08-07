package example.common

object ServerConfig {
    private val DEFAULT_PORT = "8080"
    private val DEFAULT_DATABASE_URL = "jdbc:h2:file:./example"
    private val DEFAULT_DB_DRIVER = "org.h2.Driver"
    private val DEFAULT_DATABASE_USERNAME = "sa"
    private val DEFAULT_DATABASE_PASSWORD = ""

    val port: Int
        get() = System.getProperty("server.port", DEFAULT_PORT).toInt()

    val dbUrl: String
        get() = System.getProperty("server.db.url", DEFAULT_DATABASE_URL)

    val dbDriver: String
        get() = System.getProperty("server.db.driver", DEFAULT_DB_DRIVER)

    val dbUsername: String
        get() = System.getProperty("server.db.username", DEFAULT_DATABASE_USERNAME)

    val dbPassword: String
        get() = System.getProperty("server.db.password", DEFAULT_DATABASE_PASSWORD)
}
