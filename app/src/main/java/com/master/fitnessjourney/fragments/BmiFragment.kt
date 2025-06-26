package com.master.fitnessjourney.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.master.fitnessjourney.R
import com.master.fitnessjourney.entities.BmiEntry
import com.master.fitnessjourney.repository.BmiRepository

class BmiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)

        val weightInput = view.findViewById<EditText>(R.id.editTextWeight)
        val heightInput = view.findViewById<EditText>(R.id.editTextHeight)
        val ageInput = view.findViewById<EditText>(R.id.editTextAge)
        val targetWeightInput = view.findViewById<EditText>(R.id.editTextTargetWeight)
        val radioGroupSex = view.findViewById<RadioGroup>(R.id.radioGroupSex)
        val spinnerActivity = view.findViewById<Spinner>(R.id.spinnerActivityLevel)
        val spinnerGoal = view.findViewById<Spinner>(R.id.spinnerGoal)
        val calculateButton = view.findViewById<Button>(R.id.buttonCalculate)
        val resultText = view.findViewById<TextView>(R.id.textViewResult)

        val activityLevels = listOf(
            "Alege nivelul de activitate",
            "Sedentar", "Ușor activ", "Moderat", "Activ", "Foarte activ"
        )
        val activityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, activityLevels)
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerActivity.adapter = activityAdapter

        val goals = listOf("Alege obiectivul", "Slăbire", "Menținere", "Îngrășare")
        val goalAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, goals)
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGoal.adapter = goalAdapter

        calculateButton.setOnClickListener {
            val weight = weightInput.text.toString().toFloatOrNull()
            val height = heightInput.text.toString().toFloatOrNull()
            val age = ageInput.text.toString().toIntOrNull()
            val targetWeight = targetWeightInput.text.toString().toFloatOrNull()

            val selectedSexId = radioGroupSex.checkedRadioButtonId
            val sex = when (selectedSexId) {
                R.id.radioMale -> "masculin"
                R.id.radioFemale -> "feminin"
                else -> null
            }

            val activityFactor = when (spinnerActivity.selectedItemPosition) {
                1 -> 1.2f
                2 -> 1.375f
                3 -> 1.55f
                4 -> 1.725f
                5 -> 1.9f
                else -> null
            }

            val goal = when (spinnerGoal.selectedItemPosition) {
                1 -> "slabire"
                2 -> "mentinere"
                3 -> "ingrasare"
                else -> null
            }

            if (weight != null && height != null && age != null && sex != null && activityFactor != null && goal != null) {
                val bmi = weight / ((height / 100) * (height / 100))

                // BMR
                val bmr = if (sex == "masculin") {
                    10 * weight + 6.25 * height - 5 * age + 5
                } else {
                    10 * weight + 6.25 * height - 5 * age - 161
                }

                // TDEE
                val tdee = bmr * activityFactor

                // Ajustare după obiectiv
                val calorii = when (goal) {
                    "slabire" -> tdee - 500
                    "ingrasare" -> tdee + 500
                    else -> tdee
                }

                resultText.text = "BMI: %.2f\nNecesar caloric: %.0f kcal/zi pentru $goal.".format(bmi, calorii)

                val email = requireContext()
                    .getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
                    .getString("email", "") ?: ""

                val entry = BmiEntry(
                    weight = weight,
                    height = height,
                    bmi = bmi,
                    age = age,
                    goal = goal,
                    sex = sex,
                    calories = calorii.toFloat(),
                    activityFactor = activityFactor,
                    userEmail = email
                )


                BmiRepository.insertBmi(entry) {
                    Toast.makeText(requireContext(), "BMI salvat!", Toast.LENGTH_SHORT).show()
                }

                // 🔽 Salvare în SharedPreferences (CONTEXT_DETAILS)
                val sharedPreferences = activity?.getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)!!
                sharedPreferences.edit()
                    .putFloat("weight", weight)
                    .putFloat("height", height)
                    .putInt("age", age)
                    .putFloat("bmi", bmi)
                    .putFloat("calories", calorii.toFloat())
                    .putString("goal", goal)
                    .putString("sex", sex)
                    .putFloat("activityFactor", activityFactor)

                    .apply()

            } else {
                resultText.text = "Completează toate câmpurile corect."
            }
        }

        return view
    }
}
