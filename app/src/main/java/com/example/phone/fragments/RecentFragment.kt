package com.example.phone.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.phone.adapters.CallLogAdapter
import com.example.phone.databinding.FragmentRecentBinding
import com.example.phone.utils.CallLogUtil

class RecentFragment : Fragment() {
    private lateinit var binding: FragmentRecentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRecentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CALL_LOG
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.READ_CALL_LOG), 1)
        } else {
            loadCallLogs()
        }
    }

    private fun loadCallLogs() {
        val callLogs = CallLogUtil.getCallLogs(requireContext())
        val adapter = CallLogAdapter(callLogs) { contactNumber ->
            // Handle call initiation here
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadCallLogs()
        }
    }
}
