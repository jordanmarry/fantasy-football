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

        val mostOwned = HashMap<Player, Int>()
        val allTeams = HashMap<String, Int>()
        val bestPlayers = HashMap<String, Player>()

        val leagueList = database.child("users").child(key!!).child("leagues") as ArrayList<League>

        for(l in leagueList){
            for(p in l.playerList){
                var count = mostOwned.get(p) as Int
                count += 1
                mostOwned.put(p, count)
                var teamCount = allTeams.get(p.team) as Int
                teamCount += 1
                allTeams.put(p.team, teamCount)
                if(bestPlayers.containsKey(p.pos) && bestPlayers.get(p.pos)!!.ffPoints < p.ffPoints){
                    bestPlayers.put(p.pos, p)
                } else if (!bestPlayers.containsKey(p.pos)){
                    bestPlayers.put(p.pos, p)
                }
            }
        }
        var fiveMostOwned = ArrayList<Player>()
        var threeMostTeams = ArrayList<String>()

        var mostOwnedPlayer = findMostOwned(mostOwned)
        fiveMostOwned.add(mostOwnedPlayer)
        mostOwned.remove(mostOwnedPlayer)

        mostOwnedPlayer = findMostOwned(mostOwned)
        fiveMostOwned.add(mostOwnedPlayer)
        mostOwned.remove(mostOwnedPlayer)

        mostOwnedPlayer = findMostOwned(mostOwned)
        fiveMostOwned.add(mostOwnedPlayer)
        mostOwned.remove(mostOwnedPlayer)

        mostOwnedPlayer = findMostOwned(mostOwned)
        fiveMostOwned.add(mostOwnedPlayer)
        mostOwned.remove(mostOwnedPlayer)

        mostOwnedPlayer = findMostOwned(mostOwned)
        fiveMostOwned.add(mostOwnedPlayer)
        mostOwned.remove(mostOwnedPlayer)

        var mostOwnedTeam = findMostTeams(allTeams)
        threeMostTeams.add(mostOwnedTeam )
        allTeams.remove(mostOwnedTeam )

        mostOwnedTeam  = findMostTeams(allTeams)
        threeMostTeams.add(mostOwnedTeam )
        allTeams.remove(mostOwnedTeam )

        mostOwnedTeam  = findMostTeams(allTeams)
        threeMostTeams.add(mostOwnedTeam )
        allTeams.remove(mostOwnedTeam )

        // fiveMostOwned contains five most owned player objects across leagues
        // threeMostTeams contains three most owned teams across leagues
        // bestPlayers map that contains best player at each position


    }

    private fun findMostOwned(mostOwned: HashMap<Player, Int>): Player {
        var best: Player = mostOwned.get(mostOwned.keys.first()) as Player
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
}