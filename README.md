# http4k-rest-example

An opinionated take on how to build REST services with Kotlin.

Goals:

* service should start up in less than 10 seconds
* it should be fast to develop new features
* code should be easy to test

Key components:

* http4k with Netty as engine and Jackson for JSON serialization
* Exposed used as SQL DSL (to be replaced by [sequel](https://github.com/coconautti/sequel))
* KotlinTest for writing test with Mockito for mocking

The example project can be used as a starting point for building REST services. The code contains a controller for service status and another controller for an imaginary user API. All code is packaged by feature. Common stuff is in common package.
