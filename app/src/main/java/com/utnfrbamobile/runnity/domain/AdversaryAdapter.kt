package com.utnfrbamobile.runnity.domain

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.utnfrbamobile.runnity.R

class AdversaryAdapter(private val users: List<User>, private val listener: OnAdversarySelectedListener): RecyclerView.Adapter<AdversaryAdapter.ViewHolder>() {

    lateinit var selectedLabel: TextView
    var isSelected = false

    override fun getItemCount() = users.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adversary_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val adversary = users[position]
        holder.itemView.findViewById<TextView>(R.id.adversary_name_label).text = adversary.name
        holder.itemView.setOnClickListener {
            if(isSelected){
                selectedLabel.setBackgroundColor(Color.parseColor("#0000FFFF"))
                selectedLabel.setTextColor(Color.parseColor("black"))
            }

            isSelected = true
            selectedLabel = holder.itemView.findViewById<TextView>(R.id.adversary_name_label)

            selectedLabel.setBackgroundColor(Color.parseColor("#FF6200EE"))
            selectedLabel.setTextColor(Color.parseColor("#FFFFFFFF"))
            listener.onAdversarySelected(adversary.email, adversary.name)
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
}