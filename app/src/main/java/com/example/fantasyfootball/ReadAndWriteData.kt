package com.example.fantasyfootball

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth

class ReadAndWriteData {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    fun writeUser(id: String, email: String) {
        // Get database reference
        database = Firebase.database.reference
        // Create user object and database key
        val user = User(id, email)
        val key = email.substring(0, email.indexOf('@'))
        // Add to database
        database.child("users").child(key).setValue(user)
    }

    fun writeLeague(league: League) {
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        database = Firebase.database.reference
        database.child("users").child(key!!).child("leagues")
            .child(league.leagueName).setValue(league)

    }

}