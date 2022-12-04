package com.example.fantasyfootball

data class Player(
    val name: String? = null,
    val team: String? = null,
    val pos: String? = null,
    val ppg: Double? = 0.0,
    val snapShare: Double? = 0.0,
    val passAtt: Double? = 0.0,
    val passComp: Double? = 0.0,
    val passYards: Double? = 0.0,
    val passCompPercent: Double? = 0.0,
    val passTds: Double? = 0.0,
    val ints: Double? = 0.0,
    val rating: Double? = 0.0,
    val rushAtt: Double? = 0.0,
    val rushYds: Double? = 0.0,
    val rushYdsPerAtt: Double? = 0.0,
    val rushTds: Double? = 0.0,
    val targets: Double? = 0.0,
    val receptions: Double? = 0.0,
    val recYds: Double? = 0.0,
    val recYdsPerRecep: Double? = 0.0,
    val recTds: Double? = 0.0,
    val fumbles: Double? = 0.0,
    val  fgAtt: Double? = 0.0,
    val fgMade: Double? = 0.0,
    val expMade: Double? = 0.0,
    val ffPoints: Double? = 0.0
    )

