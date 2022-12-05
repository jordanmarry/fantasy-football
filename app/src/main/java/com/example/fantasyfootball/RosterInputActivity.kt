package com.example.fantasyfootball

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class RosterInputActivity : Activity() {
    // variable declarations
    private lateinit var items: ArrayList<String>
    private lateinit var itemsAdapter: ArrayAdapter<String>
    private lateinit var lvItems: ListView
    private lateinit var database: DatabaseReference
    private lateinit var leagueName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rosterinput)

        // list view adapter
        lvItems = findViewById<View>(R.id.lvItems) as ListView
        items = arrayListOf<String>()

        itemsAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, items!!
        )
        lvItems!!.adapter = itemsAdapter

        // spinner adapters
        var psSp = findViewById<Spinner>(R.id.posSpinner) as Spinner
        var tmSp = findViewById<Spinner>(R.id.teamSpinner) as Spinner

        val posAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.positions, android.R.layout.simple_spinner_item
        )
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        psSp.adapter = posAdapter

        val teamAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.teams, android.R.layout.simple_spinner_item
        )
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


                val auth = requireNotNull(FirebaseAuth.getInstance())
                val email = auth.currentUser?.email
                val key = email?.substring(0, email.indexOf('@'))

                database = FirebaseDatabase.getInstance()
                    .getReference("users/$key/leagues/$leagueName/playerList")

                val first = items!![pos].split("\\s".toRegex())[0][0]
                val last = items!![pos].split("\\s".toRegex())[1]
                val playerName = "$first.$last"

                database.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (playerSnapshot in snapshot.getChildren()) {
                            val player = playerSnapshot.getValue(Player::class.java)!!
                            if (player.name == playerName){
                                playerSnapshot.getRef().removeValue();
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


                items!!.removeAt(pos)


                //
                // could implement delete from DB but if too much work delete
                //

                // refresh the adapter
                itemsAdapter!!.notifyDataSetChanged()
                true
            }
    }


    // add button
    fun onAddItem(v: View?) {
        val etNewItem = findViewById<View>(R.id.fName) as EditText
        val etNewItem2 = findViewById<View>(R.id.lName) as EditText
        val fName = etNewItem.text.toString()
        val lName = etNewItem2.text.toString()
        // check for player name via regex
        if (checkName(fName) && checkName(lName)) {
            // format for API
            val fC = fName[0]
            val player = "${getPos()}-$fC$lName-${getTeam()}"

            database = FirebaseDatabase.getInstance()
                .getReference("players/$player")

            database.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val p = snapshot.getValue(Player::class.java)!!
                        // display player
                        itemsAdapter!!.add("$fName $lName")
                        // reset view
                        etNewItem.setText("")
                        etNewItem2.setText("")
                        // add formatted name
                        addPlayer(p)
                    } else {
                        Toast.makeText(applicationContext, "Invalid Player. Try Again", Toast.LENGTH_LONG).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        } else {
            // throw notification if non-matching
            Toast.makeText(applicationContext, "Invalid Name Format", Toast.LENGTH_LONG).show()
        }

    }

    private fun addPlayer(player: Player) {
        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))

        database = FirebaseDatabase.getInstance()
            .getReference("users/$key/leagues/$leagueName")
        database.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val arr = arrayListOf<Player>()
                for (i in snapshot.child("playerList").children){
                    val p = i.getValue(Player::class.java)!!
                    arr.add(p)
                }
                arr.add(player)

                database.child("playerList").setValue(arr)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
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


    private fun checkName(pname: String): Boolean {
        var reg = Regex("[A-Z]+.+")
        return pname.matches(reg)
    }


    // submit button
    fun onSubmitTeam(v: View?) {
        val tName = findViewById<View>(R.id.teamName) as EditText
        val lName = findViewById<View>(R.id.leagueName) as EditText
        val teamName = tName.text.toString()
        leagueName = lName.text.toString()
        // check team and league names are supplied
        if (teamName.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext, "Please add your team name",
                Toast.LENGTH_LONG
            ).show()
        } else if (leagueName.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext, "Please add your league name",
                Toast.LENGTH_LONG
            ).show()
        }

        // add league and team name to DB
        Toast.makeText(applicationContext, "Added League and Team", Toast.LENGTH_LONG).show()
        v!!.visibility = View.GONE

        tName.inputType = 0
        tName.isFocusable = false
        tName.isFocusableInTouchMode = false
        tName.isClickable = false

        lName.inputType = 0
        lName.isFocusable = false
        lName.isFocusableInTouchMode = false
        lName.isClickable = false

        findViewById<View>(R.id.teamName).visibility = View.GONE
        findViewById<View>(R.id.leagueName).visibility = View.GONE


        findViewById<View>(R.id.fName).visibility = View.VISIBLE
        findViewById<View>(R.id.lName).visibility = View.VISIBLE
        findViewById<View>(R.id.btnAddItem).visibility = View.VISIBLE
        findViewById<View>(R.id.textView).visibility = View.VISIBLE
        findViewById<View>(R.id.posSpinner).visibility = View.VISIBLE
        findViewById<View>(R.id.textView2).visibility = View.VISIBLE
        findViewById<View>(R.id.teamSpinner).visibility = View.VISIBLE
        findViewById<View>(R.id.lvItems).visibility = View.VISIBLE
        findViewById<View>(R.id.remove).visibility = View.VISIBLE
        findViewById<View>(R.id.line1).visibility = View.VISIBLE
        findViewById<View>(R.id.line2).visibility = View.VISIBLE


        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))



        database = FirebaseDatabase.getInstance()
            .getReference("users/$key/leagues/$leagueName")

        val playerList = arrayListOf<Player>()

        database.setValue(League(leagueName, teamName, playerList))


    }


    // reset view on submission
    private fun clearView() {
        val tName = findViewById<View>(R.id.teamName) as EditText
        tName.setText("")
        val lName = findViewById<View>(R.id.leagueName) as EditText
        lName.setText("")
        val firName = findViewById<View>(R.id.fName) as EditText
        firName.setText("")
        val lasName = findViewById<View>(R.id.lName) as EditText
        lasName.setText("")

        lvItems = findViewById<View>(R.id.lvItems) as ListView
        items = ArrayList()
        itemsAdapter = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, items!!
        )
        lvItems!!.adapter = itemsAdapter
    }


}