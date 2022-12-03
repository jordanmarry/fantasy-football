package com.example.fantasyfootball

data class League (
    var leagueName: String? = null,
    var teamName: String? = null,
    var playerList: ArrayList<Player>? = arrayListOf<Player>()
)