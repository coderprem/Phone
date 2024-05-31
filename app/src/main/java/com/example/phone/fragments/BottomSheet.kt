package com.example.phone.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.phone.R
import com.example.phone.databinding.FragmentBottomSheetBinding
import com.example.phone.models.DialedNumber
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var dialedNumberViewModel: DialedNumber
    private lateinit var dialedNumberTextView: TextView
    private var dialedNumber = StringBuilder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        dialedNumberViewModel = ViewModelProvider(activity).get(DialedNumber::class.java)
        dialedNumberViewModel.dialedNumber.observe(viewLifecycleOwner) {
            dialedNumberTextView.text = it
        }

        binding.callButton.setOnClickListener {
            val numberToCall = dialedNumber.toString()
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                makeCall(numberToCall)
            } else {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_REQUEST_CODE
                )
                // Save the number temporarily to use after permission is granted
                dialedNumberViewModel.dialedNumber.value = numberToCall
            }
        }
        binding.backspaceButton.setOnClickListener {
            if (dialedNumber.isNotEmpty()) {
                dialedNumber.deleteCharAt(dialedNumber.length - 1)
                dialedNumberTextView.text = dialedNumber.toString()
            }
        }
        binding.backspaceButton.setOnLongClickListener {
            dialedNumber.clear()
            dialedNumberTextView.text = dialedNumber.toString()
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        dialedNumberTextView = binding.dialedNumber
        setupButtons()
        return binding.root
    }

    private fun setupButtons() {
        val buttons = listOf(
            binding.button0 to "0\n+",
            binding.button1 to "1\n",
            binding.button2 to "2\nABC",
            binding.button3 to "3\nDEF",
            binding.button4 to "4\nGHI",
            binding.button5 to "5\nJKL",
            binding.button6 to "6\nMNO",
            binding.button7 to "7\nPQRS",
            binding.button8 to "8\nTUV",
            binding.button9 to "9\nWXYZ",
            binding.buttonStar to "*",
            binding.buttonHash to "#"
        )

        for ((button, text) in buttons) {
            button.text = styleButtonText(text)
            button.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
            button.setOnClickListener {
                val buttonText = text.split("\n")[0]
                dialedNumber.append(buttonText)
                dialedNumberTextView.text = dialedNumber.toString()
            }
        }
    }

    private fun styleButtonText(text: String): SpannableString {
        val spannableString = SpannableString(text)
        val parts = text.split("\n")
        if (parts.size > 1) {
            val numberPart = parts[0]
            val lettersPart = parts[1]
            spannableString.setSpan(RelativeSizeSpan(2f), 0, numberPart.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(RelativeSizeSpan(0.5f), numberPart.length + 1, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannableString
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
