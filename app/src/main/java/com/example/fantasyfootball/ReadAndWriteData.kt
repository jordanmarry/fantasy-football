package com.example.fantasyfootball

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReadAndWriteData {
    private lateinit var database: DatabaseReference

    fun writeUser(id: String, email: String) {
        // Get database reference
        database = Firebase.database.reference
        // Create user object and database key
        val user = User(id, email)
        val key = email.substring(0, email.indexOf('@'))
        // Add to database
        database.child("users").child(key).setValue(user)
    }
}