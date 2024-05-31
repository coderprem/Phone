package com.example.phone.repository

import com.example.phone.db.ContactEntity
import com.example.phone.db.ContactsDao

// Repository class to manage the data
class ContactRepository(private val dao: ContactsDao) {

    val contacts = dao.getAllContacts() // LiveData object to observe the data

    // Function to insert a contact
    suspend fun insert(contact: ContactEntity) : Long{
        return dao.insert(contact)
    }

    // Function to update a contact
    suspend fun update(contact: ContactEntity){
        dao.update(contact)
    }

    // Function to delete a contact
    suspend fun delete(contact: ContactEntity){
        dao.delete(contact)
    }

    // Function to delete all contacts
    suspend fun deleteAllContacts(){
        dao.deleteAllContacts()
    }

    // Function to get a contact by name
    suspend fun getContactByName(name: String): ContactEntity{
        return dao.getContactByName(name)
    }

    // Function to get a contact by phone number
    suspend fun getContactByPhoneNumber(phoneNumber: String): ContactEntity{
        return dao.getContactByPhoneNumber(phoneNumber)
    }
}