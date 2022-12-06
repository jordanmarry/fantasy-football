package com.example.fantasyfootball

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fantasyfootball.databinding.ActivityAdviceBinding
import com.example.fantasyfootball.databinding.OverviewFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class OverviewFragment : Fragment(), PlayerClickListener {
    private lateinit var database: DatabaseReference
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var binding : OverviewFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // creating the binding root
        binding = OverviewFragmentBinding.inflate(inflater, container, false)

        getLeagueData()

        return binding.root
    }

    private fun getLeagueData(){
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues")

        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val arr = arrayListOf<League>()
                    for (i in snapshot.children){
                        val p = i.getValue(League::class.java)!!
                        arr.add(p)
                    }
                    rosterSharePercentage(arr)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    @SuppressLint("UseRequireInsteadOfGet")
    fun rosterSharePercentage(leagueList: ArrayList<League>)  {
        // find first empty team
        // database = FirebaseDatabase.getInstance().getReference("users")

        // get the current user and check to see their first empty team slot
        // once found, go through playersIn and find each player in the players database
        // then add the player obj to playerList, then add playerList to the first empty team

        val mostOwned = HashMap<Player, Int>()
        val allTeams = HashMap<String, Int>()
        var allTeamsCopy = HashMap<String, Int>()
        val bestPlayers = HashMap<String, Player>()

        // Getting the Data primed
        for (l in leagueList){
            if (l.playerList != null){
                for (p in l.playerList!!){
                    mostOwned[p] = 0
                    allTeams[p.team!!] = 0

                    if (!bestPlayers.containsKey(p.pos)){
                        bestPlayers.put(p.pos!!, p)
                    } else if(bestPlayers.containsKey(p.pos) && bestPlayers.get(p.pos)!!.ffPoints!! < p.ffPoints!!){
                        bestPlayers.put(p.pos!!, p)
                    }
                }
            }

        }

        for (l in leagueList) {
            if (l.playerList != null) {
                for (p in l.playerList!!) {
                    var count = mostOwned.get(p) as Int
                    count += 1
                    mostOwned.put(p, count)
                    var teamCount = allTeams.get(p.team) as Int
                    teamCount += 1
                    allTeams.put(p.team!!, teamCount)
                }
            }
        }

        var full = 0

        for(i in allTeams.keys){
            allTeamsCopy[i] = allTeams.get(i) as Int
        }

        for (i in allTeams.keys){
            full += allTeams[i]!!
        }

        var fiveMostOwned = ArrayList<Player>()
        var threeMostTeams = ArrayList<String>()

        while (mostOwned.size != 0 && fiveMostOwned.size < 5){
            var mostOwnedPlayer = findMostOwned(mostOwned)
            fiveMostOwned.add(mostOwnedPlayer)
            mostOwned.remove(mostOwnedPlayer)
        }

        while (allTeams.size != 0 && threeMostTeams.size < 3){
            var mostOwnedTeam = findMostTeams(allTeams)
            threeMostTeams.add(mostOwnedTeam)
            allTeams.remove(mostOwnedTeam )
        }

        val threeMostTeamsArr = arrayListOf<HashMap<String, Double>>()

        for (i in threeMostTeams){
            val hi = HashMap<String, Double>()
            hi[i] = ((allTeamsCopy[i]!!.toDouble()/full.toDouble())*100.0)
            threeMostTeamsArr.add(hi)
        }

        // Potentially Make a Team Card. Team Adapter Class.

        val teamRecyclerView = binding.teamRecyclerView
        if (threeMostTeamsArr.size > 0){
            val size = if (threeMostTeamsArr.size >= 3) 3 else threeMostTeamsArr.size
            teamRecyclerView.layoutManager = GridLayoutManager(activity, size)
            teamRecyclerView.adapter =
                this@OverviewFragment.activity?.let { TeamAdapter(threeMostTeamsArr, it.applicationContext) }
        }


        // fiveMostOwned contains five most owned player objects across leagues
        // threeMostTeams contains three most owned teams across leagues
        // bestPlayers map that contains best player at each position
        val mostRecyclerView = binding.mostRecyclerView
        mostRecyclerView.layoutManager = GridLayoutManager(activity,1)
        mostRecyclerView.adapter = this@OverviewFragment.activity?.let { PlayerAdapter(fiveMostOwned, this@OverviewFragment, it.applicationContext) }

        val bestPlayersArr = arrayListOf<Player>()
        for (i in bestPlayers.keys) {
            bestPlayersArr.add(bestPlayers[i]!!)
        }

        val topPosRecyclerView = binding.topPosRecyclerView
        topPosRecyclerView.layoutManager = GridLayoutManager(activity,1)
        topPosRecyclerView.adapter = this@OverviewFragment.activity?.let { PlayerAdapter(bestPlayersArr, this@OverviewFragment, it.applicationContext) }


    }

    private fun findMostOwned(mostOwned: HashMap<Player, Int>) : Player {
        var best: Player = mostOwned.keys.iterator().next()
        var max = 0
        for(k in mostOwned.keys){
            if(mostOwned[k]!! > max){
                max = mostOwned[k]!!
                best = k
            }
        }
        return best
    }

    private fun findMostTeams(allTeams: HashMap<String, Int>): String {
        var best = ""
        var max = 0
        for(k in allTeams.keys){
            if(allTeams[k]!! > max){
                max = allTeams[k]!!
                best = k
            }
        }
        return best
    }

    override fun onClick(player: Player) {
        val intent = Intent(activity, PlayerActivity::class.java)
        val name = player.name!!.replace(".","")
        val str = player.pos + "-" + name + "-" + player.team
        intent.putExtra("PLAYER", str)
        startActivity(intent)
    }
}