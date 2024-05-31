package com.example.phone.adapters

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.phone.R
import com.example.phone.models.CallLogEntry
import android.Manifest
import android.app.Activity
import java.text.SimpleDateFormat
import java.util.Locale

class CallLogAdapter(private val callLogs: List<CallLogEntry>, private val onCallClickListener: (String) -> Unit) :
    RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.call_log_item, parent, false)
        return CallLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogs[position]
        holder.bind(callLog)
    }

    override fun getItemCount(): Int {
        return callLogs.size
    }

    inner class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView = itemView.findViewById(R.id.contact_name)
        private val contactNumber: TextView = itemView.findViewById(R.id.mobile)
        private val callTypeIcon: ImageButton = itemView.findViewById(R.id.icon)
        private val callTime: TextView = itemView.findViewById(R.id.time_ago)
        private val callImage: TextView = itemView.findViewById(R.id.contact_image)
        private val callButton: ImageButton = itemView.findViewById(R.id.call_button)
//        private val callDuration: TextView = itemView.findViewById(R.id.call_duration)

        fun bind(callLog: CallLogEntry) {
            // Set contact name
            if (!callLog.contactName.isNullOrEmpty()) {
                contactName.text = callLog.contactName
                // Set call image text to the first character of contact name
                callImage.text = callLog.contactName.first().toString()
            } else {
                // Handle the case where contact name is null or empty
                contactName.text = "Unknown"
                // Set a default value for call image text
                callImage.text = "?" // You can use any default character or symbol
            }

            // Set other views
            contactNumber.text = "Mobile •"
            callTime.text = getTimeAgo(callLog.callTime)
            // Set call type icon
            when (callLog.callType) {
                "outgoing" -> {
                    callTypeIcon.setImageResource(R.drawable.call_made_24px)
                }
                "incoming" -> {
                    callTypeIcon.setImageResource(R.drawable.call_received_24px)
                }
                "missed" -> {
                    callTypeIcon.setImageResource(R.drawable.call_missed_24px)
                    contactNumber.setTextColor(Color.RED)   // Change text color to red for missed calls
                    callTypeIcon.setColorFilter(Color.RED)  // Change icon color to red for missed calls
                }
            }

            // Generate random color for call image background
            val randomColor = generateRandomColor()
            callImage.backgroundTintList = ColorStateList.valueOf(randomColor)
            // Set text color based on luminance
            val textColor = if (isColorDark(randomColor)) Color.WHITE else Color.BLACK
            callImage.setTextColor(textColor)

            // Handle click event for initiating call
            callButton.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        itemView.context,
                        Manifest.permission.CALL_PHONE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    makeCall(callLog.contactNumber)
                } else {
                    ActivityCompat.requestPermissions(
                        itemView.context as Activity,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        123
                    )
                }
            }
        }
        private fun makeCall(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            itemView.context.startActivity(intent)
        }

    }

    private fun getTimeAgo(callTimeMillis: Long): String {
        val now = System.currentTimeMillis()
        val timeAgo = now - callTimeMillis
        val minutesAgo = timeAgo / DateUtils.MINUTE_IN_MILLIS
        val hoursAgo = timeAgo / DateUtils.HOUR_IN_MILLIS
        val daysAgo = timeAgo / DateUtils.DAY_IN_MILLIS

        return when {
            timeAgo < DateUtils.MINUTE_IN_MILLIS -> "Just now"
            minutesAgo == 1L -> "1 minute ago"
            minutesAgo < 60 -> "$minutesAgo minutes ago"
            hoursAgo == 1L -> "1 hour ago"
            hoursAgo < 24 -> "$hoursAgo hours ago"
            daysAgo == 1L -> "Yesterday"
            daysAgo < 7 -> "$daysAgo days ago"
            else -> SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(callTimeMillis)
        }
    }


    // Function to generate a random color
    private fun generateRandomColor(): Int {
        val random = java.util.Random()
        val red = random.nextInt(256)
        val green = random.nextInt(256)
        val blue = random.nextInt(256)
        return Color.rgb(red, green, blue)
    }

    // Function to check if a color is dark
    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}
