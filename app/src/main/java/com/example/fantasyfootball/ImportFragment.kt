package com.example.fantasyfootball
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fantasyfootball.databinding.MainFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Context
import java.util.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ImportFragment: Fragment() {

    private lateinit var database: DatabaseReference
    private var writer = ReadAndWriteData()


        fun createPlayerList(teamName: String, playersIn: List<String>)  {
                // find first empty team
                // database = FirebaseDatabase.getInstance().getReference("users")

                // get the current user and check to see their first empty team slot
                // once found, go through playersIn and find each player in the players database
                // then add the player obj to playerList, then add playerList to the first empty team
                val auth = requireNotNull(FirebaseAuth.getInstance())
                val email = auth.currentUser?.email
                val key = email?.substring(0, email.indexOf('@'))
              
                val playerList = mutableListOf<Player>()
                for (currPlayer in playersIn) {
                    // find currPlayer in players database
                    val currPlayerObject = database.child("players").child(currPlayer) as Player

                   playerList.add(0, currPlayerObject)
                }

                val currUserObject = database.child("users").child(key!!) as User
                
        }

}