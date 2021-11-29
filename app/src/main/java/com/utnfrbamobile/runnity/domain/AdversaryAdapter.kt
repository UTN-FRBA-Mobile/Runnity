package com.utnfrbamobile.runnity.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utnfrbamobile.runnity.R

class AdversaryAdapter(private val users: List<User>, private val listener: OnAdversarySelectedListener): RecyclerView.Adapter<AdversaryAdapter.ViewHolder>() {

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adversary_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adversary = users[position]
        holder.itemView.findViewById<TextView>(R.id.adversary_name_label).text = adversary.name
        holder.itemView.setOnClickListener {
            listener.onAdversarySelected(adversary.email, adversary.name)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}