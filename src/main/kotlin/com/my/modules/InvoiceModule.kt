package com.my.modules

import com.my.routes.invoiceRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.invoiceModule() {
    routing {
        invoiceRoutes()
    }
}