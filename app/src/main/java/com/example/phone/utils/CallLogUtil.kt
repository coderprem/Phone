package com.example.phone.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.CallLog
import com.example.phone.models.CallLogEntry

object CallLogUtil {
    fun getCallLogs(context: Context): List<CallLogEntry> {
        val callLogs = mutableListOf<CallLogEntry>()
        val cursor: Cursor? = context.contentResolver.query(
            CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val contactNameIndex = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val contactNumberIndex = it.getColumnIndex(CallLog.Calls.NUMBER)
            val callTypeIndex = it.getColumnIndex(CallLog.Calls.TYPE)
            val callTimeIndex = it.getColumnIndex(CallLog.Calls.DATE)
            val callDurationIndex = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                val contactName = it.getString(contactNameIndex) ?: "Unknown"
                val contactNumber = it.getString(contactNumberIndex)
                val callType = when (it.getInt(callTypeIndex)) {
                    CallLog.Calls.OUTGOING_TYPE -> "outgoing"
                    CallLog.Calls.INCOMING_TYPE -> "incoming"
                    CallLog.Calls.MISSED_TYPE -> "missed"
                    else -> "unknown"
                }
                val callTime = it.getLong(callTimeIndex)
                val callDuration = it.getString(callDurationIndex) ?: "0"

                callLogs.add(CallLogEntry(contactName, contactNumber, callType, callTime, callDuration))
            }
        }

        return callLogs
    }
}
