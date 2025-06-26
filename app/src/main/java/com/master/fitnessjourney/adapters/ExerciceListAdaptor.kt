package com.master.fitnessjourney.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.R
import com.master.fitnessjourney.helpers.extensions.logErrorMessage
import com.master.fitnessjourney.models.ExerciceModel

class ExerciceListAdaptor(
    val list: List<ExerciceModel>,
    val itemChoose: (ExerciceModel) -> Unit
): RecyclerView.Adapter<ExerciceListAdaptor.ExerciceViewHolder>() {
    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciceViewHolder {
        val cellView = LayoutInflater.from(parent.context).inflate(R.layout.item_exercice_cell, parent, false)
        return ExerciceViewHolder(cellView)
    }

    override fun onBindViewHolder(holder: ExerciceViewHolder, position: Int) {
        val exercice = list.getOrNull(position)?:return //if is null then return
        holder.onBind(exercice, itemChoose)
    }

    inner class ExerciceViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val nameTextView: TextView
        private val typeTextView: TextView
        private val equipmentTextView: TextView
        private val instructionTextView: TextView
        private val muscleTextView: TextView
        private val difficultyTextView: TextView
        private val chooseButton: Button
        init {
            nameTextView = view.findViewById(R.id.tv_name)
            typeTextView = view.findViewById(R.id.tv_type)
            equipmentTextView = view.findViewById(R.id.tv_equipment)
            instructionTextView = view.findViewById(R.id.tv_instructions)
            muscleTextView = view.findViewById(R.id.tv_muscle)
            difficultyTextView = view.findViewById(R.id.tv_difficulty)
            chooseButton = view.findViewById(R.id.button_choose)
        }
        fun onBind(exercice:ExerciceModel,
                   itemChoose: (ExerciceModel) -> Unit,
                   ){
            nameTextView.text = exercice.name
            typeTextView.text = exercice.type
            equipmentTextView.text =exercice.equipment
            instructionTextView.text = exercice.instructions
            muscleTextView.text = exercice.muscle
            difficultyTextView.text = exercice.difficulty

            chooseButton.setOnClickListener {
                itemChoose(exercice)
            }
        }
    }
}