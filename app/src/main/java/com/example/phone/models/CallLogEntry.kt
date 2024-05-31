package com.example.phone.models

data class CallLogEntry(
    val contactName: String,
    val contactNumber: String,
    val callType: String,
    val callTime: Long,
    val callDuration: String
)
