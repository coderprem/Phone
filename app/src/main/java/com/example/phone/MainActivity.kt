package com.example.phone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.phone.databinding.ActivityMainBinding
import com.example.phone.databinding.FragmentBottomSheetBinding
import com.example.phone.fragments.BottomSheet
import com.example.phone.fragments.ContactsFragment
import com.example.phone.fragments.FavouriteFragment
import com.example.phone.fragments.RecentFragment
import com.example.phone.models.DialedNumber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialedNumberViewModel: DialedNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dialedNumberViewModel = ViewModelProvider(this).get(DialedNumber::class.java)
        binding.fabDial.setOnClickListener {
            val bottomSheet = BottomSheet()
            bottomSheet.show(supportFragmentManager, "BottomSheet")
        }
        dialedNumberViewModel.dialedNumber.observe(this) {
            FragmentBottomSheetBinding.inflate(layoutInflater).dialedNumber.text = it
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_favorite -> {
                    loadFragment(FavouriteFragment())
                    true
                }
                R.id.nav_recent -> {
                    loadFragment(RecentFragment())
                    true
                }
                R.id.nav_contacts -> {
                    loadFragment(ContactsFragment())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_favorite
        }
    }

    private fun loadFragment(fragment: Fragment) {
        // Load the fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, make the call
                val numberToCall = dialedNumberViewModel.dialedNumber.value
                if (!numberToCall.isNullOrEmpty()) {
                    makeCall(numberToCall)
                }
            }
        }
    }

    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        }
    }

    companion object {
        private const val CALL_PHONE_REQUEST_CODE = 123
    }
}
