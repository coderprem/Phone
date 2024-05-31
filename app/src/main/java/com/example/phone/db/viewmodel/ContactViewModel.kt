package com.example.phone.db.viewmodel

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.phone.adapters.Contact
import com.example.phone.db.ContactEntity
import com.example.phone.repository.ContactRepository
import kotlinx.coroutines.launch

// ViewModel: store and manage UI-related data in a lifecycle-conscious way
// separate the UI controller from the UI data
class ContactViewModel(private val repository: ContactRepository): ViewModel(), Observable {
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        TODO("Not yet implemented")
    }

    val contacts = repository.contacts // LiveData object to observe the data
    private var isUpdateOrDelete = false
    private lateinit var contactToUpdateOrDelete: ContactEntity

    @Bindable
    val inputName = MutableLiveData<String?>()

    @Bindable
    val inputPhoneNumber = MutableLiveData<String?>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun insert(contact: ContactEntity) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun update(contact: ContactEntity) = viewModelScope.launch {
        repository.update(contact)

        inputName.value = null
        inputPhoneNumber.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun delete(contact: ContactEntity) = viewModelScope.launch {
        repository.delete(contact)

        inputName.value = null
        inputPhoneNumber.value = null
        isUpdateOrDelete = false
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun clearAll() = viewModelScope.launch {
        repository.deleteAllContacts()
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            contactToUpdateOrDelete.name = inputName.value!!
            contactToUpdateOrDelete.phoneNumber = inputPhoneNumber.value!!

            update(contactToUpdateOrDelete)
        } else {
            val name = inputName.value!!
            val phoneNumber = inputPhoneNumber.value!!

            insert(ContactEntity(0, name, phoneNumber))
        }

        // Todo: Maybe error from video 625
        inputName.value = null
        inputPhoneNumber.value = null
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(contactToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    fun initUpdateOrDelete(contact: ContactEntity) {
        inputName.value = contact.name
        inputPhoneNumber.value = contact.phoneNumber

        isUpdateOrDelete = true
        contactToUpdateOrDelete = contact

        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }
}