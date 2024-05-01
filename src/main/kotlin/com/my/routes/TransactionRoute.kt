package com.my.routes

import com.my.models.Transaction
import com.my.models.TransactionJson
import com.my.models.Product
import com.my.models.Partner
import com.my.models.Database
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.eq
import org.litote.kmongo.or
import java.util.UUID



fun Route.transactionRoutes() {
    val database = Database()
    val transactionsCollection = database.db.getCollection<Transaction>("transactions")
    route("/transactions") {
        post {
            val transaction = call.receive<Transaction>()
            transaction._id = UUID.randomUUID().toString()
            try {
                val insertResult = transactionsCollection.insertOne(transaction)
                if (insertResult.wasAcknowledged()) {
                    call.respond(HttpStatusCode.Created, "Transaction ${transaction._id} added successfully")
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Transaction creation failed")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid transaction data")
            }
        }
        get {
            val type = call.request.queryParameters["type"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                "type is mandatory"
            )
            try {
                val transactions : List<Transaction> = when(type) {
                    "payment" -> {
                        transactionsCollection.find(or(Transaction::type eq "BPV", Transaction::type eq "CPV")).toList()
                    }

                    "receipt" -> {
                        transactionsCollection.find(or(Transaction::type eq "BRV", Transaction::type eq "CRV")).toList()
                    }

                    else -> return@get call.respond(HttpStatusCode.BadRequest, "Invalid type")
                }
                val responseList = mutableListOf<TransactionJson>()
                for (t: Transaction in transactions) {
                    val partner = when(type){
                        "payment" -> {
                            database.db.getCollection<Partner>("partners").findOneById(t.paymentTo)
                        }
                        "receipt" -> {
                            database.db.getCollection<Partner>("partners").findOneById(t.receiptFrom)
                        }
                        else -> return@get call.respond(HttpStatusCode.BadRequest, "Invalid type")
                    }
                    val jsonTransactionResponse = TransactionJson(partner, t)
                    responseList.add(jsonTransactionResponse)
                }
                val jsonTransactionResponse = Json.encodeToString(responseList)
                call.respond(HttpStatusCode.OK, jsonTransactionResponse)
                } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid transaction data")
            }
        }
    }
}