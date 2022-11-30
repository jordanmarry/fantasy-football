package com.example.fantasyfootball

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.fantasyfootball.databinding.DashboardFragmentBinding
import kotlin.collections.ArrayList
import kotlin.random.Random

class DashboardFragment : Fragment(), LeagueClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        createLeagues()

        // creating the binding root
        val binding = DashboardFragmentBinding.inflate(inflater, container, false)

        val act = this

        binding.recyclerView2.apply {
            layoutManager = GridLayoutManager(activity,2)
            adapter = LeagueAdapter(leagueList, act)
        }

        return binding.root

    } override fun onClick(league: League) {
        val intent = Intent(activity, LeagueActivity::class.java)
        intent.putExtra(LEAGUE_ID_EXTRA, league.id)
        startActivity(intent)
    }

    private fun createLeagues() {
        var writer = ReadAndWriteData()

        val league1 = League(
            "Jared",
            "Can't Read7",
            "12"
        )
        leagueList.add(league1)
        writer.writeLeague(league1)

        val league2 = League(
            "Wared2",
            "Can't Read6",
            "11"
        )
        writer.writeLeague(league2)
        leagueList.add(league2)
        val league3 = League(
            "Rared3",
            "Can't Read5",
            "13"
        )
        writer.writeLeague(league3)
        leagueList.add(league3)
        val league4 = League(
            "Fared4",
            "Can't Read4",
            "14"
        )
        leagueList.add(league4)
        val league5 = League(
            "Dared5",
            "Can't Read3",
            "15"
        )
        leagueList.add(league5)
        val league6 = League(
            "Qared6",
            "Can't Read2",
            "16"
        )
        leagueList.add(league6)
        val league7 = League(
            "Kared7",
            "Can't Read1",
            "17"
        )
        leagueList.add(league7)
    }
}