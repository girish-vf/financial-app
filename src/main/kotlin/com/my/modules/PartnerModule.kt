package com.my.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.my.routes.partnerRoutes

fun Application.partnerModule() {
    routing {
        partnerRoutes()
    }
}