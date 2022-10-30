package com.example.fantasyfootball

import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.example.fantasyfootball.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create the matches Array
        val teams: ArrayList<String> = ArrayList()
        for (i in 1..3) {
            teams.add("DanielMalone.com #$i")
        }
        // creating the binding root
        val binding = DashboardFragmentBinding.inflate(inflater, container, false)

        // Create a recyclerview
        binding.recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        // Pass the array of matches through to get displayed (MatchesAdapter.kt)
        binding.recyclerView.adapter = TeamAdapter(teams)

        // Lets the view snap into place
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)


        return binding.root
    }

    companion object {
        const val TAG = "Football"
    }

}