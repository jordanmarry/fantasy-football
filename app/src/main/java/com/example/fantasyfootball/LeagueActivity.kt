package com.example.fantasyfootball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fantasyfootball.databinding.ActivityLeagueBinding

class LeagueActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLeagueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val leagueID = intent.getIntExtra(LEAGUE_ID_EXTRA  , -1)

        val league = leagueFromID(leagueID)

        if (league != null){
            binding.leagueName.text = league.leagueName
            binding.teamName.text = league.teamName
        }
    }

    private fun leagueFromID(leagueID: Int): League? {
         for (league in leagueList) {
             if (league.id == leagueID)
                 return league
         }
        return null
    }
}