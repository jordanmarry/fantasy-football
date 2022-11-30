package com.example.fantasyfootball

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlayerDatabase(private val context: Context) :

    SQLiteOpenHelper(context, NAME, null, VERSION) {

    // ALL - name, team, pos, gp, off snaps played, off team snaps, pass attempts, pass comp,
    //       pass yards, pass comp %, pass TDs, INTs, rating, rush attemmpts, rush yards,
    //       rush yards per att, rush tds, targets, receptions, receiving yds, receivng yards per recep, receiving tds,
    //       fumbleslost, field goals att, field goals made, extra point made, ff points
    companion object {
        const val TABLE_NAME = "players"
        const val PLAYER_NAME = "name"
        const val TEAM = "team"
        const val POSITION = "position"
        const val GP = "games played"
        const val OFF_SNAPS = "off snaps played"
        const val OFF_TEAM_SNAPS = "off team snaps"
        const val PASS_ATT = "pass attempts"
        const val PASS_COMP = "pass completions"
        const val PASS_YDS = "pass yds"
        const val PASS_COMP_PERCENT = "pass comp %"
        const val PASS_TDS = "pass tds"
        const val INTS = "ints"
        const val RATING = "rating"
        const val RUSH_ATT = "rush attempts"
        const val RUSH_YDS = "rush yds"
        const val RUSH_YDS_PER_ATT = "rush yds per att"
        const val RUSH_TDS = "rush tds"
        const val TARGETS = "targets"
        const val REC = "receptions"
        const val REC_YDS = "receiving yds"
        const val REC_YDS_PER_RECEP = "receiving yds per reception"
        const val REC_TDS = "receiving tds"
        const val FUMBLES = "fumbles"
        const val FG_ATT = "fg attempts"
        const val FG_MADE = "fg made"
        const val EXP_MADE = "extra points made"
        const val FF_POINTS = "ff points"
        private const val ID = "_id"
        val columns = arrayOf(ID, PLAYER_NAME, TEAM, POSITION, GP, OFF_SNAPS, OFF_TEAM_SNAPS, PASS_ATT
                , PASS_COMP,PASS_YDS,PASS_COMP_PERCENT,PASS_TDS,INTS,RATING,RUSH_ATT,RUSH_YDS,
            RUSH_YDS_PER_ATT,RUSH_TDS,TARGETS ,REC,REC_YDS,REC_YDS_PER_RECEP,REC_TDS,FUMBLES,FG_ATT
                    ,FG_MADE,EXP_MADE,FF_POINTS)
        const val VERSION = 1
        const val CREATE_CMD = (
                "CREATE TABLE players (" + ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + PLAYER_NAME + " TEXT NOT NULL, "
                        + TEAM + " TEXT NOT NULL, "
                        + POSITION + " TEXT NOT NULL, "
                        + GP + " INTEGER, "
                        + OFF_SNAPS + " INTEGER, "
                        + OFF_TEAM_SNAPS + " INTEGER, "
                        + PASS_ATT + " INTEGER, "
                        + PASS_COMP + " INTEGER, "
                        + PASS_COMP_PERCENT + " INTEGER, "
                        + PASS_TDS + " INTEGER, "
                        + INTS + " INTEGER, "
                        + RATING + " INTEGER, "
                        + RUSH_ATT + " INTEGER, "
                        + RUSH_YDS + " INTEGER, "
                        + RUSH_YDS_PER_ATT + " INTEGER, "
                        + RUSH_TDS + " INTEGER, "
                        + TARGETS + " INTEGER, "
                        + REC + " INTEGER, "
                        + REC_YDS + " INTEGER, "
                        + REC_YDS_PER_RECEP + " INTEGER, "
                        + REC_TDS + " INTEGER, "
                        + FUMBLES + " INTEGER, "
                        + FG_ATT + " INTEGER, "
                        + FG_MADE + " INTEGER, "
                        + EXP_MADE + " INTEGER, "
                        + FF_POINTS + " INTEGER)")
        const val NAME = "player_db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_CMD)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun deleteDatabase() {
        context.deleteDatabase(NAME)
    }
}


