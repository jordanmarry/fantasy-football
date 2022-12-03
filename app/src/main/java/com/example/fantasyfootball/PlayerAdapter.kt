package com.example.fantasyfootball

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.PlayerCardCellBinding

class PlayerAdapter (private val players: List<Player>, private val clickListener: PlayerClickListener, private val context: Context) :
    RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val itemBinding = PlayerCardCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlayerViewHolder(itemBinding, clickListener, context)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bindLeague(players[position])
    }

    override fun getItemCount() = players.size

    class PlayerViewHolder(
        private val playerCardCellBinding: PlayerCardCellBinding,
        private val clickListener: PlayerClickListener,
        private val context: Context,
    ) : RecyclerView.ViewHolder(playerCardCellBinding.root) {

        fun bindLeague(player: Player){
            val pos = player.pos
            val name = player.name
            val team = player.team
            val ffpoints = player.ffPoints
            val snapshare = player.snapShare
            val ss = String.format("%.3f", snapshare)
            val ppg = player.ppg
            val ppg2 = String.format("%.3f", ppg)
            playerCardCellBinding.position.text = "Position: $pos"
            playerCardCellBinding.playerName.text = "Name: $name"
            playerCardCellBinding.teamName.text = "Team Name: $team"
            playerCardCellBinding.FFPoints.text = "FFPoints: $ffpoints"
            playerCardCellBinding.SnapShare.text = "Snap Share: $ss"
            playerCardCellBinding.PPG.text = "PPG: $ppg2"
            playerCardCellBinding.cardView.setOnClickListener{
                clickListener.onClick(player)
            }

            val uri = "@drawable/" + player.team!!.lowercase()
            Log.d("TEAM", uri)
            val v = context.resources.getIdentifier(uri, null, context.packageName)
            val ves = context.resources.getDrawable(v)
            playerCardCellBinding.teamImage.setImageDrawable(ves)
        }
    }
}