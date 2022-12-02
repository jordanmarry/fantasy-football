package com.example.fantasyfootball

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fantasyfootball.databinding.DashboardFragmentBinding
import androidx.navigation.fragment.findNavController

class DashboardFragment : Fragment(), LeagueClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // creating the binding root
        val binding = DashboardFragmentBinding.inflate(inflater, container, false)

        val act = this

        binding.recyclerView2.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = LeagueAdapter(leagueList, act)
        }

        binding.overview.setOnClickListener{
            findNavController().navigate(
                R.id.action_dashboardFragment_to_overviewFragment
            )
        }

        return binding.root

    } override fun onClick(league: League) {
        val intent = Intent(activity, LeagueActivity::class.java)
        intent.putExtra(LEAGUE_ID_EXTRA, league.id)
        startActivity(intent)
    }


}