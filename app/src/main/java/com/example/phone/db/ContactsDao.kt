package com.example.phone.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity) : Long

    @Update
    suspend fun update(contact: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)

    @Query("DELETE FROM contacts")
    suspend fun deleteAllContacts()

    @Query("Select * From contacts")
    fun getAllContacts() : LiveData<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE name LIKE :name")
    suspend fun getContactByName(name: String): ContactEntity

    @Query("SELECT * FROM contacts WHERE phoneNumber LIKE :phoneNumber")
    suspend fun getContactByPhoneNumber(phoneNumber: String): ContactEntity
}