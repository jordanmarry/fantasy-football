package com.example.fantasyfootball

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.CardCellBinding

class LeagueAdapter (private val leagues: List<League>, private val clickListener: LeagueClickListener, private val context: Context) :
    RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val itemBinding = CardCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(itemBinding, clickListener, context)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindLeague(leagues[position])
    }

    override fun getItemCount() = leagues.size

    class LeagueViewHolder(
        private val cardCellBinding: CardCellBinding,
        private val clickListener: LeagueClickListener,
        private val context: Context,
    ) : RecyclerView.ViewHolder(cardCellBinding.root) {

        fun bindLeague(league: League){
            cardCellBinding.leagueName.text = league.leagueName
            cardCellBinding.teamName.text = league.teamName

            cardCellBinding.cardView.setOnClickListener{
                clickListener.onClick(league)
            }

            cardCellBinding.cardView.setOnLongClickListener {
                val intent = Intent(context, EditLeagueActivity::class.java)
                intent.putExtra("LEAGUE_NAME", league.leagueName)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
                true
            }

        }
    }

}