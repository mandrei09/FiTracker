package com.master.fitnessjourney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.R
import com.master.fitnessjourney.entities.BmiEntry
import java.text.SimpleDateFormat
import java.util.*

class WeightHistoryAdapter(private val entries: List<BmiEntry>) :
    RecyclerView.Adapter<WeightHistoryAdapter.WeightViewHolder>() {

    class WeightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textWeight: TextView = view.findViewById(R.id.textViewWeight)
        val textDate: TextView = view.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight_history, parent, false)
        return WeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val entry = entries[position]
        holder.textWeight.text = "%.1f kg".format(entry.weight)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dateString = dateFormat.format(Date(entry.date))
        holder.textDate.text = dateString
    }

    override fun getItemCount(): Int = entries.size
}
