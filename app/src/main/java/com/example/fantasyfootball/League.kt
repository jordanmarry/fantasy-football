package com.example.fantasyfootball

var leagueList = mutableListOf<League>()

const val LEAGUE_ID_EXTRA = "leagueID"

// This will change

data class League(
    var leagueName: String,
    var teamName: String,
    var record: String,
    var id: Int? = leagueList.size,
    var playerList: ArrayList<Player>
)
