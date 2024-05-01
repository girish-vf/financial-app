package com.my.modules

import com.my.routes.ledgerRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.ledgerModule() {
    routing {
        ledgerRoutes()
    }
}