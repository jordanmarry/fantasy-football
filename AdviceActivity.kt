package com.example.fantasyfootball

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.fantasyfootball.databinding.ActivityAdviceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference


class AdviceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdviceBinding
    private  lateinit var league: League
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    val leagueID = intent.getIntExtra(LEAGUE_ID_EXTRA  , -1)

    league = leagueFromID(leagueID)!!

}

private fun leagueFromID(leagueID: Int): League? {
    for (league in leagueList) {
        if (league.id == leagueID)
            return league
    }
    return null
}

    fun sellStrongWeak(teamName: String, playersIn: List<String>)  {
        // find first empty team
        // database = FirebaseDatabase.getInstance().getReference("users")

        // get the current user and check to see their first empty team slot
        // once found, go through playersIn and find each player in the players database
        // then add the player obj to playerList, then add playerList to the first empty team
        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email

        val key = email?.substring(0, email.indexOf('@'))

        val usersPlayers = database.child("users").child(key!!).child("leagues").child(league.leagueName).child("players") as ArrayList<Player>
        var sellPlayerList = ArrayList<Player>()
        var strongPlayerList = ArrayList<ArrayList<Player>>()
        var weakPlayerList = ArrayList<ArrayList<Player>>()
        var qbPlayerList = ArrayList<Player>()
        var rbPlayerList = ArrayList<Player>()
        var wrPlayerList = ArrayList<Player>()
        var tePlayerList = ArrayList<Player>()

        for (p in usersPlayers){
            if((p.snapShare) < 0.55){
                sellPlayerList.add(p)
            }
            if (p.pos == "QB"){
                if(qbPlayerList.isEmpty()){
                    qbPlayerList.add(p)
                } else {
                    if (p.ffPoints > qbPlayerList[0].ffPoints){
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
                        if(min.ffPoints < i.ffPoints){
                            min = i
                        }
                    }
                    if(min.ffPoints < p.ffPoints){
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
                        if(min.ffPoints < i.ffPoints){
                            min = i
                        }
                    }
                    if(min.ffPoints < p.ffPoints){
                        wrPlayerList.remove(min)
                        wrPlayerList.add(p)
                    }
                }
            } else if (p.pos == "QB"){
                if(tePlayerList.isEmpty()){
                    tePlayerList.add(p)
                } else {
                    if (p.ffPoints > tePlayerList[0].ffPoints){
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
                    if(best.ffPoints < p.ffPoints){
                        best = p
                    }
                }
                sellPlayerList.remove(best)
                finalPlayerList.add(best)
            }
            sellPlayerList = finalPlayerList
        }



        if(qbPlayerList.isNotEmpty() && qbPlayerList[0].ppg >= 18) {
            strongPlayerList.add(qbPlayerList)
        } else {
            weakPlayerList.add(qbPlayerList)
        }
        if(rbPlayerList.isNotEmpty()){
            var totalPPG = 0.0
            for(i in rbPlayerList){
                totalPPG += i.ppg
            }
            totalPPG /= rbPlayerList.size
            if(totalPPG >= 12){
                strongPlayerList.add(rbPlayerList)
            } else {
                weakPlayerList.add(rbPlayerList)
            }
        } else {
            weakPlayerList.add(rbPlayerList)
        }
        if(wrPlayerList.isNotEmpty()){
            var totalPPG = 0.0
            for(i in wrPlayerList){
                totalPPG += i.ppg
            }
            totalPPG /= wrPlayerList.size
            if(totalPPG >= 12){
                strongPlayerList.add(wrPlayerList)
            } else {
                weakPlayerList.add(wrPlayerList)
            }
        } else {
            weakPlayerList.add(wrPlayerList)
        }
        if(tePlayerList.isNotEmpty() && tePlayerList[0].ppg >= 18) {
            strongPlayerList.add(tePlayerList)
        } else {
            weakPlayerList.add(tePlayerList)
        }

        // sellPlayerList will contain anywhere between 0 - 3 players that will need to be displayed
        // strongPlayerList contains array lists of each positions players that has been determined strong
        // weakPlayerList contains an array list of each positions players that has been determined strong




    }

}