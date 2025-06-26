package com.master.fitnessjourney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.R
import com.master.fitnessjourney.models.ExerciceModel

class ExerciceDoneAdaptor(
    val list: List<ExerciceModel>
): RecyclerView.Adapter<ExerciceDoneAdaptor.ExerciceDoneViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExerciceDoneViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.item_exercice_done_cell, parent, false)
        return ExerciceDoneViewHolder(cellView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ExerciceDoneViewHolder, position: Int) {
        val exerciceDone = list.getOrNull(position)?:return
        holder.onBind(exerciceDone)
    }

    inner class ExerciceDoneViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val nameTextView: TextView
        private val typeTextView: TextView
        private val equipmentTextView: TextView
        private val instructionTextView: TextView
        private val muscleTextView: TextView
        private val difficultyTextView: TextView
        init {
            nameTextView = view.findViewById(R.id.tv_name_done)
            typeTextView = view.findViewById(R.id.tv_type_done)
            equipmentTextView = view.findViewById(R.id.tv_equipment_done)
            instructionTextView = view.findViewById(R.id.tv_instructions_done)
            muscleTextView = view.findViewById(R.id.tv_muscle_done)
            difficultyTextView = view.findViewById(R.id.tv_difficulty_done)
        }
        fun onBind(exercice: ExerciceModel){
            nameTextView.text = exercice.name
            typeTextView.text = exercice.type
            equipmentTextView.text = exercice.equipment
            instructionTextView.text = exercice.instructions
            muscleTextView.text = exercice.muscle
            difficultyTextView.text = exercice.difficulty
        }
    }
}