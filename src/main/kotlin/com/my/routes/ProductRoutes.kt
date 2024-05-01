package com.my.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import com.my.models.Product
import com.my.models.Database
import org.litote.kmongo.eq

fun Route.productRoutes() {
    val database = Database()
    val productsCollection = database.db.getCollection<Product>("products")
    route("/products") {
    post {
        val product = call.receive<Product>()
        try {
            val insertResult = productsCollection.insertOne(product)
            if(insertResult.wasAcknowledged()){
                call.respond(HttpStatusCode.Created, "Product created successfully")
            } else {
                call.respond(HttpStatusCode.InternalServerError, "Product creation failed")
            }
        } catch (e: ContentTransformationException){
            call.respond(HttpStatusCode.BadRequest, "Invalid product data")
        }
    }
    put ("/{id}"){
        val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing product id")
        try{
            val updateProduct = call.receive<Product>()
            val filter = Product::_id eq id
            val updateResult = productsCollection.replaceOne(filter, updateProduct)
            if (updateResult.matchedCount > 0){
                call.respond(HttpStatusCode.OK, "Product updated successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found")
            }
        } catch (e: ContentTransformationException){
            call.respond(HttpStatusCode.BadRequest, "Invalid product data")
        }
    }
    get ("{id?}"){
        val id = call.parameters["id"]
        val status = call.parameters["status"]
        try{
        if (id != null){
            val product = productsCollection.findOneById(id)
            if(product != null){
                call.respond(product)
            } else {
                call.respond(HttpStatusCode.NotFound, "Product not found")
            }
        } else if (status != null){
            call.respond(HttpStatusCode.OK, productsCollection.find(Product::status eq status).toList())
        } else {
            call.respond(HttpStatusCode.OK, productsCollection.find().toList())
        }
        } catch (e: ContentTransformationException){
            call.respond(HttpStatusCode.BadRequest, "Invalid product data")
        }
    }}
}
