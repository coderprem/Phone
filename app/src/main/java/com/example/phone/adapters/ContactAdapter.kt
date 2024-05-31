package com.example.phone.adapters

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.phone.CreateContactActivity
import com.example.phone.R
import com.example.phone.db.ContactEntity

class ContactAdapter(private val contacts: List<ContactEntity>) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    var onItemClickListener: ((ContactEntity) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.name.text = contact.name
        holder.number.text = contact.phoneNumber
        holder.image.text = contact.name.first().toString()

        // Generate a random color
        val randomColor = generateRandomColor()
        holder.image.backgroundTintList = ColorStateList.valueOf(randomColor)

        // Set text color based on luminance
        val textColor = if (isColorDark(randomColor)) Color.WHITE else Color.BLACK
        holder.image.setTextColor(textColor)

        holder.itemView.tag = contact

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(contact)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.contact_name)
        val number: TextView = itemView.findViewById(R.id.contact_number)
        val image: TextView = itemView.findViewById(R.id.contact_image)

        init {
            itemView.setOnClickListener {
                // Handle item click
                val contact = itemView.tag as Contact
                clickListener(contact)
            }
        }

        private fun clickListener(contact: Contact) {
            // go to create contact activity
            val intent = Intent(itemView.context, CreateContactActivity::class.java)
            intent.putExtra("name", contact.name)
            intent.putExtra("number", contact.phoneNumber)
            itemView.context.startActivity(intent)
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
