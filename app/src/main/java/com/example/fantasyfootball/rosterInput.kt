package com.example.fantasyfootball

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class rosterInput : Activity() {
    // variable declarations
    private var items: ArrayList<String>? = null
    private var players: ArrayList<String>? = null
    private var itemsAdapter: ArrayAdapter<String>? = null
    private var lvItems: ListView? = null
    private lateinit var database: DatabaseReference

    // on confirmation
    private val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        submitRoster()
        clearView()
    }
    private val negativeButtonClick = { dialog: DialogInterface, which: Int -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rosterinput)

        // list view adapter
        lvItems = findViewById<View>(R.id.lvItems) as ListView
        items = ArrayList()
        itemsAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, items!!
        )
        lvItems!!.adapter = itemsAdapter

        // spinner adapters
        var psSp = findViewById<Spinner>(R.id.posSpinner) as Spinner
        var tmSp = findViewById<Spinner>(R.id.teamSpinner) as Spinner

        val posAdapter = ArrayAdapter.createFromResource(this,
            R.array.positions, android.R.layout.simple_spinner_item)
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        psSp.adapter = posAdapter

        val teamAdapter = ArrayAdapter.createFromResource(this,
            R.array.teams, android.R.layout.simple_spinner_item)
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tmSp.adapter = teamAdapter

        // remove call
        setupListViewListener()
    }


    // long click listener to remove player
    private fun setupListViewListener() {
        lvItems!!.onItemLongClickListener =
                // remove the player at pos
            OnItemLongClickListener { adapter, item, pos, id ->
                items!!.removeAt(pos)
                // refresh the adapter
                itemsAdapter!!.notifyDataSetChanged()
                true
            }
    }


    // add button
    fun onAddItem(v: View?) {
        val etNewItem = findViewById<View>(R.id.etNewItem) as EditText
        val etNewItem2 = findViewById<View>(R.id.lName) as EditText
        val fName = etNewItem.text.toString()
        val lName = etNewItem2.text.toString()
        // check for player name via regex
        if (checkName(fName) && checkName(lName)) {
            // format for API
            var fC = fName[0]
            var player = "${getPos()}-$fC$lName-${getTeam()}"
            // display player
            itemsAdapter!!.add("$fName $lName")
            // reset view
            etNewItem.setText("")
            etNewItem2.setText("")
            // add formatted name
            players?.add(player)
        } else {
            // throw notification if non-matching
            Toast.makeText(applicationContext, "Invalid Name Format", Toast.LENGTH_LONG).show()
        }
    }


    // spinners
    private fun getPos(): String {
        var psSp = findViewById<Spinner>(R.id.posSpinner) as Spinner
        return psSp.selectedItem.toString()
    }

    private fun getTeam(): String {
        var tmSp = findViewById<Spinner>(R.id.teamSpinner) as Spinner
        return tmSp.selectedItem.toString()
    }


//    // check player name
//    private fun checkFirst(pname: String): Boolean {
//        //var reg = Regex("(([A-Z]\\.?\\s?)*([A-Z][a-z]+\\.?\\s?)+([A-Z]\\.?\\s?[a-z]*)*)")
//        var reg2 = Regex("[A-Z]+[a-zA-Z]+")
//        return pname.matches(reg2)
//    }

    private fun checkName(pname: String): Boolean {
        var reg = Regex("[A-Z]+.+")
        return pname.matches(reg)
    }


    // submit button
    fun onSubmitTeam(v: View?) {
        val tName = findViewById<View>(R.id.teamName) as EditText
        val lName = findViewById<View>(R.id.leagueName) as EditText
        val teamName = tName.text.toString()
        val leagueName = lName.text.toString()
        // check team and league names are supplied
        if (teamName.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "Please add your team name",
                Toast.LENGTH_LONG).show()
        } else if (leagueName.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "Please add your league name",
                Toast.LENGTH_LONG).show()
        } else {
            submitAlert()
        }
    }


    // reset view on submission
    private fun clearView() {
        val tName = findViewById<View>(R.id.teamName) as EditText
        tName.setText("")
        val lName = findViewById<View>(R.id.leagueName) as EditText
        lName.setText("")
        val firName = findViewById<View>(R.id.etNewItem) as EditText
        firName.setText("")
        val lasName = findViewById<View>(R.id.lName) as EditText
        lasName.setText("")

        lvItems = findViewById<View>(R.id.lvItems) as ListView
        items = ArrayList()
        itemsAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, items!!
        )
        lvItems!!.adapter = itemsAdapter
        players?.clear()
    }


    // submission confirmation
    private fun submitAlert() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Confirmation")
            setMessage("Are you sure you want to submit this roster?")
            setPositiveButton("Yes", DialogInterface.OnClickListener(
                function = positiveButtonClick))
            setNegativeButton("No", negativeButtonClick)
            show()
        }
    }


    // submission after user confirmation
    private fun submitRoster() {
        val tName = findViewById<View>(R.id.teamName) as EditText
        val lName = findViewById<View>(R.id.leagueName) as EditText
        val teamName = tName.text.toString()
        val leagueName = lName.text.toString()
        // TODO: make call to jeremy's function --> import(league, team, players)
        createPlayerList(leagueName, teamName, players!!)

    }

    private fun createPlayerList(leagueName: String, teamName: String, playersIn: java.util.ArrayList<String>)  {
        // find first empty team
        // database = FirebaseDatabase.getInstance().getReference("users")

        // get the current user and check to see their first empty team slot
        // once found, go through playersIn and find each player in the players database
        // then add the player obj to playerList, then add playerList to the first empty team
        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))

        val playerList = java.util.ArrayList<Player>()
        for (currPlayer in playersIn) {
            // find currPlayer in players database
            val currPlayerObject = database.child("players").child(currPlayer) as Player

            playerList.add(0, currPlayerObject)
        }

        database.child("users").child(key!!).child("leagues").child(leagueName).setValue(League(leagueName, teamName, playerList))

    }
}