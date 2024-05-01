package com.my.models

import io.ktor.util.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

class Database {
    private val config = HoconApplicationConfig(ConfigFactory.load())
    private val connString: String = try {
        config.property("ktor.db.mongo.connectionString").getString()
    } catch (e: Exception) {
        println("Error loading MongoDB connection string: ${e.message}")
        throw e // Rethrow the exception to indicate configuration loading failure
    }

    val db = KMongo.createClient(connString).coroutine.getDatabase("fingenius")
}
