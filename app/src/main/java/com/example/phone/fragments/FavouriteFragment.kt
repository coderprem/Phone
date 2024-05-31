package com.example.phone.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.phone.GridSpacingItemDecoration
import com.example.phone.R
import com.example.phone.adapters.FavoritesAdapter
import com.example.phone.databinding.FragmentFavouriteBinding
import com.example.phone.db.ContactEntity

class FavouriteFragment : Fragment() {
    private lateinit var binding: FragmentFavouriteBinding
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoriteContacts = mutableListOf<ContactEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView with GridLayoutManager
        val layoutManager = GridLayoutManager(context, 3)
        binding.recyclerViewFavorites.layoutManager = layoutManager

        // Apply item decoration to set spacing between grid items
        val spacingInPixels =
            resources.getDimensionPixelSize(R.dimen.grid_spacing)
        binding.recyclerViewFavorites.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                spacingInPixels,
                true
            )
        )


        // Initialize Adapter
        favoritesAdapter = FavoritesAdapter(favoriteContacts)
        binding.recyclerViewFavorites.adapter = favoritesAdapter



        binding.addButton.setOnClickListener {
            // go to contacts fragment
            Toast.makeText(context, "Add a contact", Toast.LENGTH_SHORT).show()
        }

        val dummyContacts = listOf(
            ContactEntity(1, "John Doe", "1234567890"),
            ContactEntity(2, "Jane Doe", "0987654321"),
            ContactEntity(3, "Alice", "1234567890"),
            ContactEntity(4, "Bob", "0987654321"),
            ContactEntity(5, "Charlie", "1234567890"),
            ContactEntity(6, "David", "0987654321"),
            // Add more dummy contacts as needed
        )

        // Add dummy contacts to the adapter
        dummyContacts.forEach {
            favoritesAdapter.addContact(it)
        }
    }
}