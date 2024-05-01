package com.my

import com.my.plugins.*
import io.ktor.server.application.*
import com.my.modules.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val config = HoconApplicationConfig(ConfigFactory.load())
    val host = config.property("ktor.deployment.host").getString()
    val port = config.property("ktor.deployment.port").getString()
    println("Starting server on $host:$port")
    embeddedServer(Netty, host = host, port = port.toInt()) {
        module()
    }.start()
        // io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureSecurity()
    //configureRouting()
    productModule()

    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}
