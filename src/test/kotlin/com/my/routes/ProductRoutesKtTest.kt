package com.my.routes

import com.my.module
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test
import java.util.UUID


class ProductRoutesKtTest {

    @Test
    fun testPostProducts() = testApplication {
        application {
            module()
        }
        client.post("/products").apply {

        }
    }
}