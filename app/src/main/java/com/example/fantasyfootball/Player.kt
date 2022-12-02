package com.example.fantasyfootball

data class Player(
    val name: String,
    val team: String,
    val pos: String,
    val ppg: Double,
    val snapShare: Double,
    val passAtt: Double,
    val passComp: Double,
    val passYards: Double,
    val passCompPercent: Double,
    val passTds: Double,
    val ints: Double,
    val rating: Double,
    val rushAtt: Double,
    val rushYds: Double,
    val rushYdsPerAtt: Double,
    val rushTds: Double,
    val targets: Double,
    val receptions: Double,
    val recYds: Double,
    val recYdsPerRecep: Double,
    val recTds: Double,
    val fumbles: Double,
    val  fgAtt: Double,
    val fgMade: Double,
    val expMade: Double,
    val ffPoints: Double
    )

