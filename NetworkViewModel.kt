package com.example.fantasyfootball

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener


class NetworkViewModel() : ViewModel() {

    /** Modifiable live data for internal use only. */
    private val _statusLiveData = MutableLiveData<Status>()
    private var writer = ReadAndWriteData()
    /** Immutable LiveData for external publishing/observing. */
    val statusLiveData: LiveData<Status>
        get() = _statusLiveData

    /** Keep track of coroutine so that duplicate requests are ignored. */
    private var job: Job? = null

    sealed class Status {
        data class Progress(val progress: Int) : Status()
        data class Result(val list: List<String>) : Status()
        data class Error(val errorResId: Int, val e: Exception) : Status()
    }

    /**
     * Non-blocking function called by the UI (Fragment) when the
     * user clicks the send button. This function starts a coroutine
     * that sends a network request and then posts the JSON result
     * on the LiveData feed that can be observed by the calling
     * Fragment.
     */
    fun sendNetworkRequest() {
        // Ignore button click if a request is still active.
        if (job?.isActive == true) {
            return
        }


        // Launch a new coroutine to run network request in the background.
        job = viewModelScope.launch {
            try {
                // 1. Run the suspending network request.
                val rawJson = makeNetworkCall(URL)
                parseJsonString(rawJson)
            } catch (e: Exception) {
                // Something went wrong ... post error to LiveData feed.
                //_statusLiveData.postValue(Status.Error(R.string.send_request_error, e))
            }
        }
    }

    /**
     * Suspending helper function that performs the network request
     * specified by the passed [url] and returns the raw JSON result.
     */
    private suspend fun makeNetworkCall(url: String): String =
        withContext(Dispatchers.IO) {
            // Construct a new Ktor HttpClient to perform the get
            // request and then return the JSON result.
            String(HttpClient().get(url).body(), charset("UTF-8"))
        }


    private fun parseJsonString(data: String?): List<String> {
        val result = ArrayList<String>()

        try {
            // Get top-level JSON Object - a Map
            val responseObject = JSONTokener(
                data
            ).nextValue() as JSONObject

            // Extract value of "earthquakes" key -- a List
            val array = responseObject.getJSONArray("players")

            //dbHelper = PlayerDatabase(this)

            // Iterate over earthquakes list
            for (idx in 0 until array.length()) {

                // Get single earthquake mData - a Map
                val currPlayer = array.get(idx) as JSONObject

                // Summarize earthquake mData as a string and add it to
                // result

                // QBs - name, team, pos, gp, pass attempts, pass comp,
                // pass yards, pass comp %, pass TDs, INTs, rating, rush yds, rush tds, fumbleslost, ff points
                if (currPlayer.get(POS_TAG) == "QB" || currPlayer.get(POS_TAG) == "RB" || currPlayer.get(POS_TAG) == "WR" ||
                    currPlayer.get(POS_TAG) == "TE" || currPlayer.get(POS_TAG) == "K"
                ) {

                    writer.writePlayer(currPlayer.get(NAME_TAG) as String, currPlayer.get(TEAM_TAG)as String,
                        currPlayer.get(POS_TAG)as String,currPlayer.get(GP_TAG)as Int, currPlayer.get(OFF_SNAP_PLAYED_TAG) as Int,
                    currPlayer.get(OFF_TEAM_SNAP_TAG)as Int, currPlayer.get(PASS_ATT_TAG)as Double
                        , currPlayer.get(PASS_COMP_TAG)as Double, currPlayer.get(PASS_YDS_TAG)as Double
                        , currPlayer.get(PASS_COMP_PERCENT_TAG)as Double, currPlayer.get(PASS_TD_TAG)as Double
                    , currPlayer.get(INT_TAG)as Double, currPlayer.get(RATING_TAG)as Double
                    , currPlayer.get(RUSH_ATT_TAG)as Double, currPlayer.get(RUSH_YD_TAG)as Double
                    , currPlayer.get(RUSH_YD_PER_ATT_TAG)as Double, currPlayer.get(RUSH_TD_TAG)as Double
                    , currPlayer.get(REC_TAR_TAG)as Double, currPlayer.get(REC_TAG)as Double
                    , currPlayer.get(REC_YD_TAG)as Double, currPlayer.get(REC_YD_PER_REC_TAG)as Double
                    , currPlayer.get(REC_TD_TAG)as Double, currPlayer.get(FUMBLE_TAG)as Double
                    , currPlayer.get(FG_ATT_TAG)as Double, currPlayer.get(FG_MADE_TAG)as Double
                    , currPlayer.get(EXP_MADE_TAG)as Double, currPlayer.get(FF_PTS_TAG)as Double)


                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * Constants
     */
    companion object {
        private const val URL = "https://jharley19.github.io/RFL-Webpage/PlayerData.json"

        const val GP_TAG = "Played"
        const val POS_TAG = "Position"
        const val TEAM_TAG = "Team"
        const val NAME_TAG = "Name"
        const val OFF_SNAP_PLAYED_TAG = "OffensiveSnapsPlayed"
        const val OFF_TEAM_SNAP_TAG = "OffensiveTeamSnaps"
        const val PASS_ATT_TAG = "PassingAttempts"
        const val PASS_COMP_TAG = "PassingCompletions"
        const val PASS_YDS_TAG  = "PassingYards"
        const val PASS_COMP_PERCENT_TAG  = "PassingCompletionPercentage"
        const val PASS_TD_TAG  = "PassingTouchdowns"
        const val INT_TAG = "PassingInterceptions"
        const val RATING_TAG = "PassingRating"
        const val RUSH_YD_TAG = "RushingYards"
        const val RUSH_TD_TAG = "RushingTouchdowns"
        const val FUMBLE_TAG = "FumblesLost"
        const val FF_PTS_TAG = "FantasyPoints"
        const val RUSH_ATT_TAG = "RushingAttempts"
        const val RUSH_YD_PER_ATT_TAG = "RushingYardsPerAttempt"
        const val REC_TAR_TAG  = "ReceivingTargets"
        const val REC_TAG = "Receptions"
        const val REC_YD_TAG = "ReceivingYards"
        const val REC_YD_PER_REC_TAG = "ReceivingYardsPerReception"
        const val REC_TD_TAG = "ReceivingTouchdowns"
        const val FG_ATT_TAG= "FieldGoalsAttempted"
        const val FG_MADE_TAG = "FieldGoalsMade"
        const val EXP_MADE_TAG = "ExtraPointsMade"

        // QBs - name, team, pos, gp, off snaps played, off team snaps, pass attempts, pass comp,
        // pass yards, pass comp %, pass TDs, INTs, rating, rush yds, rush tds, fumbleslost, ff points
        // RBs - name, team, pos, gp, off snaps played, off team snaps, rush attemmpts, rush yards,
        // rush yards per att, rush tds, targets, receptions, receiving yds, receiving tds, fumbles lost, ff points
        // WR/TE - name, team, pos, gp, off snaps played, off team snaps, rush attemmpts, rush yards,
        // rush yards per att, rush tds, targets, receptions, receiving yds, receivng yards per recep, receiving tds, fumbles lost, ff points
        // K -  name, team, pos, gp, field goals att, field goals made, extra point made, ff points
        // DST - ???

        // ALL - name, team, pos, gp, off snaps played, off team snaps, pass attempts, pass comp,
        //       pass yards, pass comp %, pass TDs, INTs, rating, rush attemmpts, rush yards,
        //       rush yards per att, rush tds, targets, receptions, receiving yds, receivng yards per recep, receiving tds,
        //       fumbleslost, field goals att, field goals made, extra point made, ff points
            /*
                            result.add(
                                NAME_TAG + ":"
                                        + array.get(NAME_TAG) + ","
                                        + TEAM_TAG + ":"
                                        + array.getString(TEAM_TAG) + ","
                                        + POS_TAG + ":"
                                        + array.get(POS_TAG)
                                        + GP_TAG + ":"
                                        + array.get(GP_TAG)
                                        + PASS_ATT_TAG + ":"
                                        + array.get(PASS_ATT_TAG)
                                        + PASS_COMP_TAG + ":"
                                        + array.get(PASS_COMP_TAG)
                                        + PASS_YDS_TAG + ":"
                                        + array.get(PASS_YDS_TAG)
                                        + PASS_COMP_PERCENT_TAG + ":"
                                        + array.get(PASS_COMP_PERCENT_TAG)
                                        + PASS_TD_TAG + ":"
                                        + array.get(PASS_TD_TAG)
                                        + INT_TAG+ ":"
                                        + array.get(INT_TAG)
                                        + RATING_TAG + ":"
                                        + array.get(RATING_TAG)
                                        + RUSH_ATT_TAG + ":"
                                        + array.get(RUSH_ATT_TAG)
                                        + RUSH_YD_TAG + ":"
                                        + array.get(RUSH_YD_TAG)
                                        + RUSH_YD_PER_ATT_TAG + ":"
                                        + array.get(RUSH_YD_PER_ATT_TAG)
                                        + RUSH_TD_TAG + ":"
                                        + array.get(RUSH_TD_TAG)
                                        + REC_TAR_TAG + ":"
                                        + array.get(REC_TAR_TAG)
                                        + REC_TAG + ":"
                                        + array.get(REC_TAG)
                                        + REC_YD_TAG + ":"
                                        + array.get(REC_YD_TAG)
                                        + REC_YD_PER_REC_TAG + ":"
                                        + array.get(REC_YD_PER_REC_TAG)
                                        + REC_TD_TAG + ":"
                                        + array.get(REC_TD_TAG)
                                        + FUMBLE_TAG + ":"
                                        + array.get(FUMBLE_TAG)
                                        + FG_ATT_TAG + ":"
                                        + array.get(FG_ATT_TAG)
                                        + FG_MADE_TAG + ":"
                                        + array.get(FG_MADE_TAG)
                                        + EXP_MADE_TAG + ":"
                                        + array.get(EXP_MADE_TAG)
                                        + FF_PTS_TAG + ":"
                                        + array.get(FF_PTS_TAG)

                            )
                        } */

    }

}
