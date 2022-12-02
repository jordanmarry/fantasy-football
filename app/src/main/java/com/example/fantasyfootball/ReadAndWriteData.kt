package com.example.fantasyfootball

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class ReadAndWriteData {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    fun writeUser(id: String, email: String) {
        // Get database reference
        database = Firebase.database.reference
        // Create user object and database key
        val user = User(id, email)
        val key = email.substring(0, email.indexOf('@'))
        // Add to database
        database.child("users").child(key).setValue(user)
    }

    fun writeLeague(league: League) {
        auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))
        database = Firebase.database.reference
        database.child("users").child(key!!).child("leagues")
            .child(league.leagueName).setValue(league)

    }

    // ALL - name, team, pos, gp, off snaps played, off team snaps, pass attempts, pass comp,
    //       pass yards, pass comp %, pass TDs, INTs, rating, rush attemmpts, rush yards,
    //       rush yards per att, rush tds, targets, receptions, receiving yds, receivng yards per recep, receiving tds,
    //       fumbleslost, field goals att, field goals made, extra point made, ff points
    fun writePlayer(name: String, team: String, pos: String, ppg: Double, snapShare: Double, passAtt: Double, passComp: Double, passYards: Double, passCompPercent: Double,
                    passTds: Double, ints: Double, rating: Double, rushAtt: Double, rushYds: Double, rushYdsPerAtt: Double, rushTds: Double,
                    targets: Double, receptions: Double, recYds: Double, recYdsPerRecep: Double, recTds: Double, fumbles: Double,
                    fgAtt: Double, fgMade: Double, expMade: Double, ffPoints: Double) {
        // Get database reference
        database = Firebase.database.reference

        // Create player object and database key
        val player = Player(name,team, pos, ppg, snapShare, passAtt, passComp, passYards, passCompPercent,
            passTds, ints, rating, rushAtt, rushYds, rushYdsPerAtt, rushTds,
            targets, receptions, recYds, recYdsPerRecep, recTds, fumbles,
            fgAtt, fgMade, expMade, ffPoints)

        val name1 = name.replace(".", "")
        val key = "$pos-$name1-$team"

        // Add to database
        database.child("players").child(key).setValue(player)
    }

}
