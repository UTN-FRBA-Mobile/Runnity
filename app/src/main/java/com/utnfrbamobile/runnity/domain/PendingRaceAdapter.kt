package com.utnfrbamobile.runnity.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.utnfrbamobile.runnity.R

class PendingRaceAdapter: RecyclerView.Adapter<PendingRaceAdapter.ViewHolder>() {
    var data = listOf<PendingRace>()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.detail.text = item.category + " | " + userName + " VS " + item.opponent

        holder.participateButton.setOnClickListener {
            // (Pendiente)
            /*val intent: Intent = Intent()
            intent.putExtra("raceId", data[position].raceId)
            intent.putExtra("opponent", data[position].opponent)
            intent.putExtra("category", data[position].category)*/

            it.findNavController().navigate(R.id.action_competitionMenuFragment_to_competitionDetailFragment)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.pending_race_view, parent, false)
        return ViewHolder(view)
    }

    var userName = "Juan Perez"

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val detail: TextView = itemView.findViewById(R.id.pendingRaceDetail)
        val participateButton: LinearLayout = itemView.findViewById(R.id.participateButton)
    }
}