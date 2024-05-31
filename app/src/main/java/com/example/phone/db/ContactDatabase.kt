package com.example.phone.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactEntity::class], version = 1)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao // abstract function to return the ContactsDao

    companion object {
        private const val DATABASE_NAME = "contacts_db"

        @Volatile
        private var instance: ContactDatabase? = null

        fun getDatabase(context: Context): ContactDatabase {
            return instance ?: synchronized(this) { // synchronized block to avoid multiple instances
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext, // avoid memory leaks
                    ContactDatabase::class.java,    // reference to the database class
                    DATABASE_NAME   // database name
                ).build()
                instance = tempInstance // assign the instance to the tempInstance
                tempInstance    // return the tempInstance
            }
        }
    }
}
