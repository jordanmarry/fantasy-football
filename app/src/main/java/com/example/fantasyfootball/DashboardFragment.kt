package com.example.fantasyfootball

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.DashboardFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class DashboardFragment : Fragment(), LeagueClickListener {
    private lateinit var binding: DashboardFragmentBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var leagueRecyclerView: RecyclerView
    private lateinit var leagueArrayList: ArrayList<League>
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use the provided ViewBinding class to inflate the layout.
        binding = DashboardFragmentBinding.inflate(inflater, container, false)

        leagueRecyclerView = binding.recyclerView

        leagueRecyclerView.layoutManager = GridLayoutManager(activity,2)

        leagueArrayList = arrayListOf<League>()

        getLeagueData( )

        // Return the root view.
        return binding.root
    }

    override fun onClick(league: League) {
        val intent = Intent(activity, LeagueActivity::class.java)
        intent.putExtra("LEAGUE_NAME", league.leagueName)
        startActivity(intent)
    }

    private fun getLeagueData() {
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues")

        dbref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(leagueSnapshot in snapshot.children){
                        val league = leagueSnapshot.child("leagueName").value
                        val team = leagueSnapshot.child("teamName").value
                        // when the JSON IS READY. THIS CAN CHANGE TO val league = snapshot.getValue(League::class.java)
                        leagueArrayList.add(League(league.toString(),team.toString(), arrayListOf<Player>()))

                    }
                    leagueRecyclerView.adapter = LeagueAdapter(leagueArrayList, this@DashboardFragment)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


}
