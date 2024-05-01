package com.my.modules

import com.my.routes.transactionRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.transactionModule() {
    routing {
        transactionRoutes()
    }
}