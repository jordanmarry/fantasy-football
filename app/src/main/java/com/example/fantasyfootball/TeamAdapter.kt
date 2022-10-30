package com.example.fantasyfootball

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.RowPostBinding

// Takes in the array
class TeamAdapter(private val team: ArrayList<String>) :
    RecyclerView.Adapter<TeamAdapter.TeamsViewHolder>() {

    // creates the view holder for the text
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val itemBinding = RowPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamsViewHolder(itemBinding)
    }

    // Assigns the text to the xml
    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        holder.teams.text = team[position]
    }

    //get size of team array
    override fun getItemCount() = team.size

    // assign the view with the binding.teams
    class TeamsViewHolder(item_view: RowPostBinding) : RecyclerView.ViewHolder(item_view.root) {
        val teams = item_view.teams
    }

}