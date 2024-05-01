package com.my.routes

import com.my.models.Partner
import com.my.models.Database
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import org.litote.kmongo.eq
import org.litote.kmongo.and





fun Route.partnerRoutes() {
    val database = Database()
    val partnersCollection = database.db.getCollection<Partner>("partners")
    route("/partners") {
        post {
            val partner = call.receive<Partner>()
            try {
                val insertResult = partnersCollection.insertOne(partner)
                if (insertResult.wasAcknowledged()) {
                    call.respond(
                        HttpStatusCode.Created,
                        "${partner.type.replaceFirstChar { it.uppercase() }} added  created successfully: ${partner.firstName} - ${partner.lastName}"
                    )
                } else {
                    call.respond(HttpStatusCode.InternalServerError, "Partner creation failed")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid partner data")
            }
        }
        get {
            val id = call.request.queryParameters["id"]
            val type = (call.request.queryParameters["type"])?.lowercase()
            val statusParam = (call.request.queryParameters["status"])?.replaceFirstChar { it.uppercase() }
            try {
                if (id != null) {
                    val partner: Partner? = partnersCollection.findOneById(id)
                    if (partner != null) {
                        call.respond(partner)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Partner not found for given id")
                    }
                } else if (type != null) {
                    if (statusParam != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            partnersCollection.find(and(Partner::type eq type, Partner::status eq statusParam)).toList()
                        )
                    } else {
                        call.respond(HttpStatusCode.OK, partnersCollection.find(Partner::type eq type).toList())
                    }
                } else {
                    call.respond(HttpStatusCode.OK, partnersCollection.find().toList())
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid partner data")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "Missing partner id")
            try {
                val updatePartner = call.receive<Partner>()
                val filter = Partner::_id eq id
                val updateResult = partnersCollection.replaceOne(filter, updatePartner)
                if (updateResult.matchedCount > 0) {
                    call.respond(
                        HttpStatusCode.OK,
                        "Partner ${updatePartner.firstName} - ${updatePartner.lastName} updated successfully"
                    )
                } else {
                    call.respond(HttpStatusCode.NotFound, "Partner not found")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid partner data")
            }
        }
    }
}