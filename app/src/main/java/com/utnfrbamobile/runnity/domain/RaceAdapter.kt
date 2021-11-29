package com.utnfrbamobile.runnity.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.utnfrbamobile.runnity.R

class RaceAdapter: RecyclerView.Adapter<RaceAdapter.ViewHolder>() {
    var data = listOf<Race>()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.pending_race_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val race = data[position]
        holder.detail.text = race.category.toString() + "K | " + race.user1.name + " VS " + race.user2.name

        holder.title.text =
            if(race.user1.duration == 0L || race.user2.duration == 0L)
                "Carrera pendiente"
            else
                "Carrera finalizada"

        holder.participateButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_competitionMenuFragment_to_competitionDetailFragment)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.pending_race_label)
        val detail: TextView = itemView.findViewById(R.id.pendingRaceDetail)
        val participateButton: LinearLayout = itemView.findViewById(R.id.participateButton)
    }
}