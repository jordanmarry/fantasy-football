package com.example.fantasyfootball


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase




class ReadAndWriteData {
    private lateinit var database: DatabaseReference

    fun writeUser(id: String, email: String) {
        // Get database reference
        database = Firebase.database.reference
        // Create user object and database key
        val user = User(id, email)
        val key = email.substring(0, email.indexOf('@'))
        // Add to database
        database.child("users").child(key).setValue(user)
    }

    // ALL - name, team, pos, gp, off snaps played, off team snaps, pass attempts, pass comp,
    //       pass yards, pass comp %, pass TDs, INTs, rating, rush attemmpts, rush yards,
    //       rush yards per att, rush tds, targets, receptions, receiving yds, receivng yards per recep, receiving tds,
    //       fumbleslost, field goals att, field goals made, extra point made, ff points
    fun writePlayer(name: String, team: String, pos: String, gp: Int, offSnapsPlayed: Int,
    offTeamSnaps: Int, passAtt: Double, passComp: Double, passYards: Double, passCompPercent: Double,
    passTds: Double, ints: Double, rating: Double, rushAtt: Double, rushYds: Double, rushYdsPerAtt: Double, rushTds: Double,
    targets: Double, receptions: Double, recYds: Double, recYdsPerRecep: Double, recTds: Double, fumbles: Double,
    fgAtt: Double, fgMade: Double, expMade: Double, ffPoints: Double) {
        // Get database reference
        database = Firebase.database.reference
        // Create user object and database key

        val player = Player(name,team, pos, gp, offSnapsPlayed,
            offTeamSnaps, passAtt, passComp, passYards, passCompPercent,
            passTds, ints, rating, rushAtt, rushYds, rushYdsPerAtt, rushTds,
            targets, receptions, recYds, recYdsPerRecep, recTds, fumbles,
            fgAtt, fgMade, expMade, ffPoints)

        val name1 = name.replace(".","")
        val key = "$pos-$name1-$pos"

        // Add to database
        database.child("players").child(key).setValue(player)
    }



}

