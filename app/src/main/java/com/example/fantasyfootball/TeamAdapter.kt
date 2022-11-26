package com.example.fantasyfootball

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.RowCellBinding

// Takes in the array
class TeamAdapter(private val team: ArrayList<Pair<Int, Int>>) :
    RecyclerView.Adapter<TeamAdapter.TeamsViewHolder>() {

    // creates the view holder for the text
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        val itemBinding = RowCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamsViewHolder(itemBinding)
    }

    // Assigns the text to the xml
    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        val (x, y) = team[position]
        holder.teams1.setImageResource(x)
        holder.teams2.setImageResource(y)
    }

    //get size of team array
    override fun getItemCount() = team.size

    // POTENTIALLY CHANGE TO LOOK LIKE LEAGUE ADAPTER
    // assign the view with the binding.teams
    class TeamsViewHolder(item_view: RowCellBinding) : RecyclerView.ViewHolder(item_view.root) {
        val teams1 = item_view.team1
        val teams2 = item_view.team2
    }

}