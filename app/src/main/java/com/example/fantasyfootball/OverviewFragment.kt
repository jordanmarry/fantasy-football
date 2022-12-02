package com.example.fantasyfootball

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fantasyfootball.databinding.OverviewFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class OverviewFragment : Fragment() {
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // creating the binding root
        val binding = OverviewFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    fun rosterSharePercentage()  {
        // find first empty team
        // database = FirebaseDatabase.getInstance().getReference("users")

        // get the current user and check to see their first empty team slot
        // once found, go through playersIn and find each player in the players database
        // then add the player obj to playerList, then add playerList to the first empty team
        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email

        val key = email?.substring(0, email.indexOf('@'))

        val mostOwned = HashMap<String, Int>()

        val leagueList = database.child("users").child(key!!).child("leagues") as ArrayList<League>


    }
}