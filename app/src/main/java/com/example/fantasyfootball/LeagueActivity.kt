package com.example.fantasyfootball

import AdviceActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.ActivityLeagueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LeagueActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLeagueBinding
    private lateinit var leagueName : String
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var playerRecyclerView: RecyclerView

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

        playerRecyclerView = binding.recyclerView

        playerRecyclerView.layoutManager = GridLayoutManager(this,1)

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

                    // NOW BASICALLY TAKE THE PLAYERS LIST AND CREATE ALL THE PLAYER CELL CARDS
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}