package com.example.fantasyfootball

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.example.fantasyfootball.databinding.ActivityPlayerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text

class PlayerActivity : AppCompatActivity(){
    private lateinit var binding : ActivityPlayerBinding
    private lateinit var playerDB: String
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerDB = intent.getStringExtra("PLAYER")!!

        getPlayerData()
    }

    private fun getPlayerData(){
        dbref = FirebaseDatabase.getInstance().getReference("players/$playerDB")

        dbref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val linLay = binding.linearLayout
                    for(playerSnapshot in snapshot.children){
                        if (playerSnapshot.key!! != "team" && playerSnapshot.key!! != "name" && playerSnapshot.key!! != "pos"){
                            val textView = TextView(this@PlayerActivity)
                            textView.text = playerSnapshot.key!! + ": " + playerSnapshot.value.toString()
                            textView.layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT)
                            textView.gravity = Gravity.CENTER
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                            textView.setPadding(5)
                            linLay.addView(textView)
                        } else if (playerSnapshot.key!! == "team"){
                            val v = playerSnapshot.value.toString()
                            binding.teamName.text = "Team: $v"

                        } else if (playerSnapshot.key!! == "name") {
                            binding.playerName.text = playerSnapshot.value.toString()
                        } else if (playerSnapshot.key!! == "pos") {
                            val v = playerSnapshot.value.toString()
                            binding.position.text = "Position: $v"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}