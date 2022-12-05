package com.example.fantasyfootball

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.ActivityLeagueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LeagueActivity : AppCompatActivity(), PlayerClickListener {

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

    override fun onClick(player: Player) {
        val intent = Intent(this, PlayerActivity::class.java)
        val name = player.name!!.replace(".","")
        val str = player.pos + "-" + name + "-" + player.team
        intent.putExtra("PLAYER", str)
        startActivity(intent)
    }

    private fun getLeagueData(){
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues/$leagueName")

        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val league = snapshot.getValue(League::class.java)!!

                    binding.leagueName.text = league.leagueName
                    binding.teamName.text = league.teamName

                    // NOW BASICALLY TAKE THE PLAYERS LIST AND CREATE ALL THE PLAYER CELL CARDS
                    playerRecyclerView.adapter = PlayerAdapter(league.playerList!!, this@LeagueActivity, this@LeagueActivity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}