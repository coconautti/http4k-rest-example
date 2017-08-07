# http4k-rest-example

An opinionated take on how to build REST services with Kotlin.

Goals:

* service should start up in less than 10 seconds
* it should be fast to develop new features
* code should be easy to test

Key components:

* [http4k](https://www.http4k.org/) with Netty as engine and Jackson for JSON serialization
* [sequel](https://github.com/coconautti/sequel) used as SQL DSL
* [KotlinTest](https://github.com/kotlintest/kotlintest) for writing test with [Mockito](https://github.com/mockito/mockito) for mocking

The example project can be used as a starting point for building REST services. The code contains a controller for service status and another controller for an imaginary user API. All code is packaged by feature. Common stuff is in common package.
