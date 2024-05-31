package com.example.phone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.phone.databinding.ActivityCreateContactBinding
import com.example.phone.db.ContactDatabase
import com.example.phone.db.ContactEntity
import com.example.phone.fragments.BottomSheet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateContactBinding

    private val database by lazy { ContactDatabase.getDatabase(this) }
    private val contactDao by lazy { database.contactsDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.close.setOnClickListener{
            finish()
        }

        // Get the data from the intent
        val intentName = intent.getStringExtra("name")
        val intentNumber = intent.getStringExtra("number")

        // Set the data to the views
        binding.name.setText(intentName)
        binding.number.setText(intentNumber)

        if (intent.getBooleanExtra("isUpdate", false)) {
            binding.saveButton.text = "Update"

            binding.saveButton.setOnClickListener {
                val updatedName = binding.name.text.toString()
                val updatedNumber = binding.number.text.toString()

                // Update the contact in the database
                CoroutineScope(Dispatchers.IO).launch {
                    contactDao.update(ContactEntity(id = intent.getIntExtra("id", 0), name = updatedName, phoneNumber = updatedNumber))
                }

                finish()
            }
        } else {
            binding.saveButton.setOnClickListener {
                val name = binding.name.text.toString()
                val number = binding.number.text.toString()

                // Insert the contact to the database
                if (name.isNotEmpty() && number.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        contactDao.insert(ContactEntity(name = name, phoneNumber = number))
                    }
                }

                finish()
            }
        }

        binding.callButton.setOnClickListener{
            val number = binding.number.text.toString()
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                makeCall(number)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_REQUEST_CODE
                )
            }
        }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
    companion object {
        private const val CALL_PHONE_REQUEST_CODE = 123
    }
}