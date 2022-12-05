package com.example.fantasyfootball

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.TeamCardCellBinding

class TeamAdapter(private val team: ArrayList<HashMap<String, Double>>, private val context: Context):
    RecyclerView.Adapter<TeamAdapter.TeamViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val itemBinding = TeamCardCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamViewHolder(itemBinding, context)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bindTeam(team[position])
    }

    override fun getItemCount() = team.size

    class TeamViewHolder(
        private val teamCardCellBinding: TeamCardCellBinding,
        private val context: Context) :
        RecyclerView.ViewHolder(teamCardCellBinding.root) {
        fun bindTeam(team: HashMap<String, Double>){
            val uri = "@drawable/" + team.iterator().next().key.lowercase()
            val v = context.resources.getIdentifier(uri, null, context.packageName)
            val ves = context.resources.getDrawable(v)

            teamCardCellBinding.teamImage.setImageDrawable(ves)
            teamCardCellBinding.teamName.text = team.iterator().next().key

            val percent = team.iterator().next().value
            val p = String.format("%.2f", percent)

            teamCardCellBinding.percentage.text = "Stake: $p%"
        }
    }

}