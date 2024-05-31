package com.example.phone.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DialedNumber: ViewModel() {

    var dialedNumber = MutableLiveData<String>()
}