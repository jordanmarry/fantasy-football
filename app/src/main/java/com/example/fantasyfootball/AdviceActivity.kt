package com.example.fantasyfootball

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fantasyfootball.databinding.ActivityAdviceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AdviceActivity : AppCompatActivity(), PlayerClickListener{

    private lateinit var binding : ActivityAdviceBinding
    private  lateinit var league: League
    private lateinit var leagueName : String
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        leagueName = intent.getStringExtra("LEAGUE_NAME")!!

        getLeagueData()
    }

    private fun getLeagueData(){
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues/$leagueName")

        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val arr = arrayListOf<Player>()
                    for (i in snapshot.child("playerList").children){
                        val p = i.getValue(Player::class.java)!!
                        arr.add(p)
                    }
                    sellStrongWeak(arr)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun sellStrongWeak(usersPlayers: ArrayList<Player>)  {
        // find first empty team
        // database = FirebaseDatabase.getInstance().getReference("users")

        // get the current user and check to see their first empty team slot
        // once found, go through playersIn and find each player in the players database
        // then add the player obj to playerList, then add playerList to the first empty team


        var sellPlayerList = ArrayList<Player>()
        var strongPlayerList = ArrayList<Player>()
        var weakPlayerList = ArrayList<Player>()
        var qbPlayerList = ArrayList<Player>()
        var rbPlayerList = ArrayList<Player>()
        var wrPlayerList = ArrayList<Player>()
        var tePlayerList = ArrayList<Player>()

        for (p in usersPlayers){
            if((p.snapShare)!! < 0.55 && p.pos != "K") {
                sellPlayerList.add(p)
            }
            if (p.pos == "QB"){
                if(qbPlayerList.isEmpty()){
                    qbPlayerList.add(p)
                } else {
                    if (p.ffPoints!! > qbPlayerList[0].ffPoints!!){
                        qbPlayerList.clear()
                        qbPlayerList.add(p)
                    }
                }
            } else if (p.pos == "RB"){
                if(rbPlayerList.size < 3){
                    rbPlayerList.add(p)
                } else {
                    var min = rbPlayerList[0]
                    for(i in rbPlayerList){
                        if(min.ffPoints!! < i.ffPoints!!){
                            min = i
                        }
                    }
                    if(min.ffPoints!! < p.ffPoints!!){
                        rbPlayerList.remove(min)
                        rbPlayerList.add(p)
                    }
                }
            } else if (p.pos == "WR"){
                if(wrPlayerList.size < 4){
                    wrPlayerList.add(p)
                } else {
                    var min = wrPlayerList[0]
                    for(i in wrPlayerList){
                        if(min.ffPoints!! < i.ffPoints!!){
                            min = i
                        }
                    }
                    if(min.ffPoints!! < p.ffPoints!!){
                        wrPlayerList.remove(min)
                        wrPlayerList.add(p)
                    }
                }
            } else if (p.pos == "TE"){
                if(tePlayerList.isEmpty()){
                    tePlayerList.add(p)
                } else {
                    if (p.ffPoints!! > tePlayerList[0].ffPoints!!){
                        tePlayerList.clear()
                        tePlayerList.add(p)
                    }
                }
            }
        }

        if (sellPlayerList.size > 3) {
            val finalPlayerList = ArrayList<Player>()
            for (i in 1..3) {
                var best = sellPlayerList[0]
                for(p in sellPlayerList){
                    if(best.ffPoints!! < p.ffPoints!!){
                        best = p
                    }
                }
                sellPlayerList.remove(best)
                finalPlayerList.add(best)
            }
            sellPlayerList = finalPlayerList
        }



        if(qbPlayerList.isNotEmpty()) {
            if (qbPlayerList[0].ppg!! >= 17) {
                strongPlayerList.add(qbPlayerList[0])

            } else {
                weakPlayerList.add(qbPlayerList[0])

            }
        }

        if(rbPlayerList.isNotEmpty()){
            for(i in rbPlayerList) {
                if (i.ppg!! >= 8.6) {
                    strongPlayerList.add(i)
                } else {
                    weakPlayerList.add(i)
                }
            }
        }

        if(wrPlayerList.isNotEmpty()){
            for(i in wrPlayerList) {
                if (i.ppg!! >= 7.1) {
                    strongPlayerList.add(i)
                } else {
                    weakPlayerList.add(i)
                }
            }
        }

        if(tePlayerList.isNotEmpty()){
            if (tePlayerList[0].ppg!! >= 5) {
                strongPlayerList.add(tePlayerList[0])
            } else {
                weakPlayerList.add(tePlayerList[0])
            }
        }

        Log.d("HERE", "SELL: $sellPlayerList")
        Log.d("HERE", "STRONG: $strongPlayerList")
        Log.d("HERE", "WEAK: $weakPlayerList")


        val sellRecyclerView = binding.sellRecyclerView
        sellRecyclerView.layoutManager = GridLayoutManager(this,1)
        sellRecyclerView.adapter = PlayerAdapter(sellPlayerList, this@AdviceActivity, this@AdviceActivity)


        val weakRecyclerView = binding.weakRecyclerView
        weakRecyclerView.layoutManager = GridLayoutManager(this,1)
        weakRecyclerView.adapter = PlayerAdapter(weakPlayerList, this@AdviceActivity, this@AdviceActivity)

        val strongRecyclerView = binding.strongRecyclerView
        strongRecyclerView.layoutManager = GridLayoutManager(this,1)
        strongRecyclerView.adapter = PlayerAdapter(strongPlayerList, this@AdviceActivity, this@AdviceActivity)

        // sellPlayerList will contain anywhere between 0 - 3 players that will need to be displayed
        // strongPlayerList contains array lists of each positions players that has been determined strong
        // weakPlayerList contains an array list of each positions players that has been determined strong




    }

    override fun onClick(player: Player) {
        val intent = Intent(this, PlayerActivity::class.java)
        val name = player.name!!.replace(".","")
        val str = player.pos + "-" + name + "-" + player.team
        intent.putExtra("PLAYER", str)
        startActivity(intent)
    }

}