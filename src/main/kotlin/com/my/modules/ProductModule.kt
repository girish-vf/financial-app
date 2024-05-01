package com.my.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.my.routes.productRoutes

fun Application.productModule() {
    routing {
        productRoutes()
    }
}