package com.master.fitnessjourney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.R
import com.master.fitnessjourney.entities.ExerciceProgress
import com.master.fitnessjourney.models.ExerciceInProgress

class ExerciceProgressAdaptor(
    val list: MutableList<ExerciceInProgress>,
    val deleteClick: (ExerciceInProgress) -> Unit,
    val updateClick: (ExerciceInProgress) -> Unit
): RecyclerView.Adapter<ExerciceProgressAdaptor.ExerciceProgressViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciceProgressViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.item_exercice_progress_cell, parent, false)
        return ExerciceProgressViewHolder(cellView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ExerciceProgressViewHolder, position: Int) {
        val exerciceProgress = list.getOrNull(position)?:return
        holder.onBind(exerciceProgress)
    }

    inner class ExerciceProgressViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val nameTextView: TextView
        private val equipmentTextView: TextView
        private val instructionsTextView: TextView
        private val deleteButton: Button
        private val doneButton: Button
        init{
            nameTextView = view.findViewById(R.id.tv_name_exc)
            equipmentTextView = view.findViewById(R.id.tv_equipment_exc)
            instructionsTextView = view.findViewById(R.id.tv_instructions_exc)
            deleteButton = view.findViewById(R.id.button_delete)
            doneButton = view.findViewById(R.id.button_done)
        }
        fun onBind(exerciceProgress: ExerciceInProgress){
            nameTextView.text = exerciceProgress.name
            equipmentTextView.text = exerciceProgress.equipment
            instructionsTextView.text = exerciceProgress.instructions
            deleteButton.setOnClickListener{
                deleteClick(exerciceProgress)
            }

            doneButton.setOnClickListener {
                updateClick(exerciceProgress)
            }
        }
    }
    fun removeItem(exercice: ExerciceInProgress) {
        val position = list.indexOf(exercice)
        if (position >= 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateItem(exercice: ExerciceInProgress) {
        val position = list.indexOf(exercice)
        if (position >= 0) {
            list[position] = exercice
            notifyItemChanged(position)
        }
    }
}