package com.example.fantasyfootball

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        binding.overview.setOnClickListener{
            findNavController().navigate(
                R.id.action_dashboardFragment_to_overviewFragment
            )
        }

        binding.createLeague.setOnClickListener {
            val intent = Intent(activity, RosterInputActivity::class.java)
            startActivity(intent)
        }

        // Return the root view.
        return binding.root
    }

    override fun onClick(league: League) {
        val intent = Intent(activity, LeagueActivity::class.java)
        intent.putExtra("LEAGUE_NAME", league.leagueName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getLeagueData()
    }


    private fun getLeagueData() {
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        dbref = FirebaseDatabase.getInstance().getReference("users/$key/leagues")
        leagueArrayList.clear()

        dbref.addValueEventListener(object: ValueEventListener{
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onDataChange(snapshot: DataSnapshot) {
                leagueArrayList.clear()
                if (snapshot.exists()){
                    for(leagueSnapshot in snapshot.children){
                        val league = leagueSnapshot.getValue(League::class.java)!!
                        // when the JSON IS READY. THIS CAN CHANGE TO val league = snapshot.getValue(League::class.java)
                        leagueArrayList.add(league)

                    }
                    leagueRecyclerView.adapter = this@DashboardFragment.context?.let { LeagueAdapter(leagueArrayList, this@DashboardFragment, it.applicationContext) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}
