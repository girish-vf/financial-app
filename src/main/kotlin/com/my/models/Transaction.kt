package com.my.models


import kotlinx.serialization.Serializable
@Serializable
data class Transaction(
    var _id: String? = null,
    val voucherNo: String,
    val type: String,
    val date: String,
    val paymentTo: String,
    val receiptFrom: String,
    val amount: String,
    val reference: String
)
@Serializable
data class TransactionJson(
    val partner: Partner?,
    val transaction: Transaction
)
