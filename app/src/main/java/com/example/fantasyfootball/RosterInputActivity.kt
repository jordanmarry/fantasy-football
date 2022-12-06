package com.example.fantasyfootball

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class RosterInputActivity : Activity() {
    // variable declarations
    private lateinit var database: DatabaseReference
    private lateinit var leagueName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rosterinput)
    }

    // submit button
    fun onSubmitTeam(v: View?) {
        val tName = findViewById<View>(R.id.teamName) as EditText
        val lName = findViewById<View>(R.id.leagueName) as EditText
        val teamName = tName.text.toString()
        leagueName = lName.text.toString()
        // check team and league names are supplied
        if (teamName.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext, "Please add your team name",
                Toast.LENGTH_LONG
            ).show()
        } else if (leagueName.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext, "Please add your league name",
                Toast.LENGTH_LONG
            ).show()
        }

        // add league and team name to DB
        Toast.makeText(applicationContext, "Added League and Team", Toast.LENGTH_LONG).show()


        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))



        database = FirebaseDatabase.getInstance()
            .getReference("users/$key/leagues/$leagueName")

        val playerList = arrayListOf<Player>()

        database.setValue(League(leagueName, teamName, playerList))

        val intent = Intent(this, EditLeagueActivity::class.java)
        intent.putExtra("LEAGUE_NAME", leagueName)
        startActivity(intent)
    }
}