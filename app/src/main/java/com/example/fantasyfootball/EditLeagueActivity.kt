package com.example.fantasyfootball

import android.app.Activity
import android.util.Log
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import com.google.firebase.database.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fantasyfootball.databinding.ActivityEditleagueBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class EditLeagueActivity: AppCompatActivity() {
    private lateinit var binding : ActivityEditleagueBinding
    private  lateinit var league: League
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var items: ArrayList<String>
    private lateinit var itemsAdapter: ArrayAdapter<String>
    private lateinit var lvItems: ListView
    private lateinit var database: DatabaseReference
    private lateinit var leagueName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditleagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        leagueName = intent.getStringExtra("LEAGUE_NAME")!!

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

            val first = items!![pos].split("\\s".toRegex())[0]
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

        val auth = requireNotNull(FirebaseAuth.getInstance())
        val email = auth.currentUser?.email
        val key = email?.substring(0, email.indexOf('@'))

        database = FirebaseDatabase.getInstance()
            .getReference("users/$key/leagues/$leagueName/playerList")

        database.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (player in snapshot.children){
                        val p = player.getValue(Player::class.java)!!
                        val fC = p.name!![0]
                        val last = p.name.subSequence(2,p.name.length)
                        itemsAdapter!!.add("$fC $last ${p.pos} ${p.team}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


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
                        itemsAdapter!!.add("$fC $lName ${getPos()} ${getTeam()}")
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
}