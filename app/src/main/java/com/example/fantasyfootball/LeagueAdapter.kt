package com.example.fantasyfootball

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fantasyfootball.databinding.CardCellBinding
import android.util.Log

class LeagueAdapter (private val leagues: List<League>, private val clickListener: LeagueClickListener) :
    RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val itemBinding = CardCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(itemBinding, clickListener )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindLeague(leagues[position])
    }

    override fun getItemCount() = leagues.size

    class LeagueViewHolder(
        private val cardCellBinding: CardCellBinding,
        private val clickListener: LeagueClickListener
    ) : RecyclerView.ViewHolder(cardCellBinding.root) {

        fun bindLeague(league: League){
            cardCellBinding.leagueName.text = league.leagueName
            cardCellBinding.teamName.text = league.teamName
            cardCellBinding.record.text = league.record

            cardCellBinding.cardView.setOnClickListener{
                clickListener.onClick(league)
            }
        }
    }


}