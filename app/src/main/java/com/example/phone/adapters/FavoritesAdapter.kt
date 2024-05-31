package com.example.phone.adapters

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.phone.R
import com.example.phone.db.ContactEntity
import java.util.jar.Manifest

class FavoritesAdapter(private val favoriteContacts: MutableList<ContactEntity>) :
    RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactImage: TextView = itemView.findViewById(R.id.contact_image)
        val contactName: TextView = itemView.findViewById(R.id.contact_name)
        val contactNumber: TextView = itemView.findViewById(R.id.contact_number)

        private fun makeCall(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_contact_item, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val contact = favoriteContacts[position]
        holder.contactName.text = contact.name
        holder.contactNumber.text = contact.phoneNumber

        // Set the first letter of the contact name as the image
        holder.contactImage.text = contact.name.first().toString()

        // Generate a random color
        val randomColor = generateRandomColor()
        holder.contactImage.backgroundTintList = ColorStateList.valueOf(randomColor)

        // Set text color based on luminance
        val textColor = if (isColorDark(randomColor)) Color.WHITE else Color.BLACK
        holder.contactImage.setTextColor(textColor)

        // Set click listener on the item view
        holder.itemView.setOnClickListener {
            // Extract phone number associated with the clicked contact
            val phoneNumber = contact.phoneNumber

            // Initiate a call using Intent
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")

            if(ContextCompat.checkSelfPermission(holder.itemView.context, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                holder.itemView.context.startActivity(intent)
            } else {
                ActivityCompat.requestPermissions(holder.itemView.context as Activity, arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteContacts.size
    }

    fun addContact(contact: ContactEntity) {
        if (favoriteContacts.size >= 6) {
            favoriteContacts.removeAt(0) // Remove the first contact
        }
        favoriteContacts.add(contact)
        notifyDataSetChanged() // Notify the adapter to update the RecyclerView
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