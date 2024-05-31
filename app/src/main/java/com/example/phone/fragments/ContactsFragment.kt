package com.example.phone.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.Uri.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phone.CreateContactActivity
import com.example.phone.R
import com.example.phone.adapters.ContactAdapter
import com.example.phone.databinding.FragmentContactsBinding
import com.example.phone.db.ContactDatabase
import com.example.phone.db.ContactEntity
import com.example.phone.db.viewmodel.ContactViewModel
import com.example.phone.db.viewmodel.ViewModelFactory
import com.example.phone.repository.ContactRepository
import java.util.jar.Manifest
import com.example.phone.adapters.SwipeToCallCallback



class ContactsFragment : Fragment() {
    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactViewModel: ContactViewModel
    private lateinit var adapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createContactLayout.setOnClickListener {
            val intent = Intent(context, CreateContactActivity::class.java)
            startActivity(intent)
        }

        // Room database
        val dao = ContactDatabase.getDatabase(requireContext()).contactsDao()
        val repository = ContactRepository(dao)
        val factory = ViewModelFactory(repository)

        // ViewModel
        contactViewModel = ViewModelProvider(this, factory).get(ContactViewModel::class.java)
        contactViewModel = ContactViewModel(repository)

        // RecyclerView
        initRecyclerView()

        // check if the user is coming from the favorite fragment
        val fromFavoriteFragment = arguments?.getBoolean("fromFavoriteFragment") ?: false
        if (fromFavoriteFragment) {
            // add a click lister to the contact clicked
            adapter.onItemClickListener = { contact ->
                // go back to the favorite fragment with the contact clicked
                val bundle = Bundle()
                bundle.putString("name", contact.name)
                bundle.putString("number", contact.phoneNumber)
                findNavController().navigate(R.id.favouriteFragment, bundle)
            }
        }

    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        displayContactsList()

    }

    private fun displayContactsList() {
        contactViewModel.contacts.observe(viewLifecycleOwner, Observer {
            val contactsList = it.map { contact ->
                ContactEntity(contact.id, contact.name, contact.phoneNumber)
            }.sortedBy { contact ->
                contact.name // Sort by name alphabetically
            }
            binding.recyclerView.adapter = ContactAdapter(contactsList)
            adapter = binding.recyclerView.adapter as ContactAdapter
            adapter.onItemClickListener = { contact ->
                val intent = Intent(context, CreateContactActivity::class.java)
                intent.putExtra("name", contact.name)
                intent.putExtra("number", contact.phoneNumber)
                intent.putExtra("id", contact.id)
                intent.putExtra("isUpdate", true)
                startActivity(intent)
                Toast.makeText(context, "Clicked: ${contact.name}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}