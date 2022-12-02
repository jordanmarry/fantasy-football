package com.example.fantasyfootball

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasyfootball.databinding.ActivityLeagueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LeagueActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLeagueBinding
    private lateinit var leagueName : String
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        leagueName = intent.getStringExtra("LEAGUE_NAME")!!

        binding.advice.setOnClickListener{
            val intent = Intent(this, AdviceActivity::class.java)
            intent.putExtra("LEAGUE_NAME", leagueName)
            startActivity(intent)
        }

        getLeagueData()
    }

    private fun getLeagueData(){
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues/$leagueName")

        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    // when the JSON IS READY. THIS CAN CHANGE TO val league = snapshot.getValue(League::class.java)
                    binding.leagueName.text =  snapshot.child("leagueName").value.toString()
                    binding.teamName.text = snapshot.child("teamName").value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}