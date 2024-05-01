package com.my.routes

import com.my.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.text.SimpleDateFormat
import org.litote.kmongo.eq
import org.litote.kmongo.or
import java.math.BigDecimal

fun Route.ledgerRoutes(){
    val database = Database()
    val invoicesCollection = database.db.getCollection<Invoice>("invoices")
    val transactionsCollection = database.db.getCollection<Transaction>("transactions")
    val partnersCollection = database.db.getCollection<Partner>("partners")

    route("/ledger"){
        // Route to get ledger items
        get {
            val partnerId = call.request.queryParameters["partnerId"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing Partner ID")
            val startDateString = call.request.queryParameters["startDate"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing start date")
            val endDateString = call.request.queryParameters["endDate"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing end date")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val startDate = dateFormat.parse(startDateString)
            val endDate = dateFormat.parse(endDateString)

            try{
                val partner = partnersCollection.findOneById(partnerId)
                val allInvoices = invoicesCollection.find(Invoice::partnerId eq partnerId).toList()
                val filteredInvoices = allInvoices.filter {
                        invoice:Invoice ->
                    val invoiceDate = dateFormat.parse(invoice.invoiceDate)
                    invoiceDate in startDate..endDate
                }
                val invoices = filteredInvoices.map { invoice:Invoice ->
                    LedgerItem(
                        date = invoice.invoiceDate,
                        instrumentNo = invoice.invoiceNo,
                        reference = invoice.reference,
                        description = if (invoice.type == "sales") "Sales" else "Purchase",
                        quantity = invoice.invoiceItems.count().toString(),
                        debit = if (invoice.type == "sales") invoice.invoiceTotal else "0",
                        credit = if (invoice.type == "purchase") invoice.invoiceTotal else "0",
                        balance = invoice.invoiceTotal
                    )
                }
                // Fetch all transactions
                val allTransactions = transactionsCollection.find(or(Transaction::paymentTo eq partnerId, Transaction::receiptFrom eq partnerId)).toList()
                val filteredTransactions = allTransactions.filter { transaction: Transaction ->
                    val date = dateFormat.parse(transaction.date)
                    date in startDate..endDate
                }

                // Map all transactions to ledger items
                val transactions = filteredTransactions.map { transaction: Transaction ->
                    val (description, debit, credit) = when (transaction.type) {
                        "BRV", "CRV" -> Triple("Receipt", BigDecimal.ZERO, transaction.amount)
                        "BPV", "CPV" -> Triple("Payment", transaction.amount, BigDecimal.ZERO)
                        else -> Triple("", BigDecimal.ZERO, BigDecimal.ZERO)
                    }

                    LedgerItem(
                        date = transaction.date,
                        instrumentNo = transaction.voucherNo,
                        reference = transaction.reference,
                        description = description,
                        quantity = "0",
                        debit = debit.toString(),
                        credit = credit.toString(),
                        balance = credit.toString(),
                    )
                }

                // Combine the transformed records from both collections into a single list
                val ledgerItems: List<LedgerItem> = (invoices + transactions).sortedBy { LocalDate.parse(it.date) }
                // Respond with the list of invoices with their corresponding partners
                val jsonResponse = Json.encodeToString(LedgerJson(partner, ledgerItems))
                call.respond(HttpStatusCode.OK, jsonResponse)

            } catch ( e: Exception){
                call.respond(HttpStatusCode.InternalServerError, "Failed")
            }

        }
    }
}