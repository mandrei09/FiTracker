package com.master.fitnessjourney.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.ApplicationController
import com.master.fitnessjourney.R
import com.master.fitnessjourney.adapters.WeightHistoryAdapter
import com.master.fitnessjourney.entities.BmiEntry
import com.master.fitnessjourney.repository.ExercicesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class DiaryFragment : Fragment() {

    private lateinit var textViewBmi: TextView
    private lateinit var textViewCalories: TextView
    private lateinit var editTextNewWeight: EditText
    private lateinit var buttonUpdateWeight: Button
    private lateinit var editTextBreakfast: EditText
    private lateinit var editTextLunch: EditText
    private lateinit var editTextDinner: EditText
    private lateinit var buttonCalculateCalories: Button
    private lateinit var textViewTotalCalories: TextView
    private lateinit var textViewCaloriesLeft: TextView
    private lateinit var textViewCaloriesBurned: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextWaterIntake: EditText
    private lateinit var buttonSaveWater: Button
    private lateinit var textViewSavedWater: TextView

    private var caloriesRecommended: Float = 0f
    private var lastEntryGlobal: BmiEntry? = null
    private val dailyGoal = 2000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary, container, false)
        createNotificationChannel()

        textViewBmi = view.findViewById(R.id.textViewBmi)
        textViewCalories = view.findViewById(R.id.textViewCalories)
        editTextNewWeight = view.findViewById(R.id.editTextNewWeight)
        buttonUpdateWeight = view.findViewById(R.id.buttonUpdateWeight)
        textViewCaloriesBurned = view.findViewById(R.id.textViewCaloriesBurned)

        editTextBreakfast = view.findViewById(R.id.editTextBreakfast)
        editTextLunch = view.findViewById(R.id.editTextLunch)
        editTextDinner = view.findViewById(R.id.editTextDinner)
        buttonCalculateCalories = view.findViewById(R.id.buttonCalculateCalories)
        textViewTotalCalories = view.findViewById(R.id.textViewTotalCalories)
        textViewCaloriesLeft = view.findViewById(R.id.textViewCaloriesLeft)

        editTextWaterIntake = view.findViewById(R.id.editTextWaterIntake)
        buttonSaveWater = view.findViewById(R.id.buttonSaveWater)
        textViewSavedWater = view.findViewById(R.id.textViewSavedWater)

        recyclerView = view.findViewById(R.id.recyclerViewWeightHistory)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val email = requireContext().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
            .getString("email", "") ?: ""
        val todayKey = "WATER_${email}_${getTodayDateKey()}"

        val prefs = requireContext().getSharedPreferences("WATER_TRACKER", Context.MODE_PRIVATE)
        var savedValue = prefs.getInt(todayKey, 0)
        textViewSavedWater.text = "Apă consumată azi: ${savedValue} ml"

        buttonSaveWater.setOnClickListener {
            val amount = editTextWaterIntake.text.toString().toIntOrNull()
            if (amount != null && amount > 0) {
                val newTotal = savedValue + amount
                val remaining = (dailyGoal - newTotal).coerceAtLeast(0)
                prefs.edit().putInt(todayKey, newTotal).apply()
                savedValue = newTotal
                textViewSavedWater.text = "Apă consumată azi: ${newTotal} ml"
                editTextWaterIntake.text.clear()
                Toast.makeText(requireContext(), "Cantitate apă salvată!", Toast.LENGTH_SHORT).show()

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    requireContext().checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    val builder = NotificationCompat.Builder(requireContext(), "WATER_CHANNEL")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Progres hidratare")
                        .setContentText("Felicitări! Ai băut $newTotal ml de apă. Mai ai $remaining ml până la obiectiv.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    NotificationManagerCompat.from(requireContext()).notify(1, builder.build())
                } else {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
                }
            } else {
                Toast.makeText(requireContext(), "Introdu o cantitate validă", Toast.LENGTH_SHORT).show()
            }
        }

        buttonCalculateCalories.setOnClickListener {
            val breakfast = editTextBreakfast.text.toString().toIntOrNull() ?: 0
            val lunch = editTextLunch.text.toString().toIntOrNull() ?: 0
            val dinner = editTextDinner.text.toString().toIntOrNull() ?: 0
            val calendar = Calendar.getInstance()
            val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
            val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
            val year = calendar.get(Calendar.YEAR).toString()
            val dayKey = getTodayDateKey()
            val username = requireContext()
                .getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
                .getString("email", null)
            if (username == null) {
                Toast.makeText(requireContext(), "Utilizator necunoscut", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            ExercicesRepository.getExcDoneByDateUsername(day, month, year, username) { exercices ->
                var caloriesBurned = 0
                for (ex in exercices) {
                    caloriesBurned += when (ex.difficulty) {
                        "BEGINNER" -> 50
                        "INTERMEDIATE" -> 100
                        "EXPERT" -> 150
                        else -> 0
                    }
                }

                val total = breakfast + lunch + dinner
                val remaining = (caloriesRecommended + caloriesBurned) - total

                requireActivity().runOnUiThread {
                    textViewTotalCalories.text = "Total consumat: $total kcal"
                    textViewCaloriesBurned.text = "Calorii arse din exerciții: $caloriesBurned kcal"
                    textViewCaloriesLeft.text = "Calorii rămase: ${remaining.toInt()} kcal (+$caloriesBurned kcal din exerciții)"
                }
            }

        }

        buttonUpdateWeight.setOnClickListener {
            val newWeight = editTextNewWeight.text.toString().toFloatOrNull()
            if (newWeight == null || newWeight <= 0f) {
                Toast.makeText(requireContext(), "Introdu o greutate validă!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use last entry or SharedPreferences for other fields
            val prefs = requireContext().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
            val email = prefs.getString("email", "") ?: ""
            val height = prefs.getFloat("height", lastEntryGlobal?.height ?: 0f)
            val age = prefs.getInt("age", lastEntryGlobal?.age ?: 0)
            val sex = prefs.getString("sex", lastEntryGlobal?.sex ?: "masculin") ?: "masculin"
            val activityFactor = prefs.getFloat("activityFactor", lastEntryGlobal?.activityFactor ?: 1.2f)
            val goal = prefs.getString("goal", lastEntryGlobal?.goal ?: "mentinere") ?: "mentinere"

            if (height == 0f || age == 0) {
                Toast.makeText(requireContext(), "Completează datele de bază în secțiunea BMI!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bmi = newWeight / ((height / 100) * (height / 100))
            val bmr = if (sex == "masculin") {
                10 * newWeight + 6.25 * height - 5 * age + 5
            } else {
                10 * newWeight + 6.25 * height - 5 * age - 161
            }
            val tdee = bmr * activityFactor
            val calorii = when (goal) {
                "slabire" -> tdee - 500
                "ingrasare" -> tdee + 500
                else -> tdee
            }

            val entry = BmiEntry(
                weight = newWeight,
                height = height,
                bmi = bmi,
                age = age,
                goal = goal,
                sex = sex,
                calories = calorii.toFloat(),
                activityFactor = activityFactor,
                userEmail = email
            )

            com.master.fitnessjourney.repository.BmiRepository.insertBmi(entry) {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Greutate și BMI actualizate!", Toast.LENGTH_SHORT).show()
                    editTextNewWeight.text.clear()
                    loadData()
                    loadWeightHistory()
                }
            }
        }

        loadData()
        loadWeightHistory()
        restoreCaloriesState(email, getTodayDateKey())
        return view
    }

    private fun saveCaloriesState(email: String, dayKey: String, total: Int, burned: Int, remaining: Int) {
        val prefs = requireContext().getSharedPreferences("CALORIE_TRACKER", Context.MODE_PRIVATE)
        prefs.edit()
            .putInt("${email}_${dayKey}_TOTAL", total)
            .putInt("${email}_${dayKey}_BURNED", burned)
            .putInt("${email}_${dayKey}_REMAINING", remaining)
            .apply()
    }

    private fun restoreCaloriesState(email: String, dayKey: String) {
        val prefs = requireContext().getSharedPreferences("CALORIE_TRACKER", Context.MODE_PRIVATE)
        val total = prefs.getInt("${email}_${dayKey}_TOTAL", -1)
        val burned = prefs.getInt("${email}_${dayKey}_BURNED", -1)
        val remaining = prefs.getInt("${email}_${dayKey}_REMAINING", -1)

        if (total != -1 && burned != -1 && remaining != -1) {
            textViewTotalCalories.text = "Total consumat: $total kcal"
            textViewCaloriesBurned.text = "Calorii arse din exerciții: $burned kcal"
            textViewCaloriesLeft.text = "Calorii rămase: $remaining kcal (+$burned kcal din exerciții)"
        }
    }

    private fun getTodayDateKey(): String {
        val calendar = Calendar.getInstance()
        val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
        val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
        val year = calendar.get(Calendar.YEAR).toString()
        return "${day}${month}${year}"
    }

    private fun loadWeightHistory() {
        CoroutineScope(Dispatchers.IO).launch {
            val email = requireContext().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
                .getString("email", "") ?: ""
            val entries = ApplicationController.instance?.appDatabase?.bmiDao()?.getAllSuspend(email) ?: emptyList()
            val sorted = entries.sortedByDescending { it.date }
            requireActivity().runOnUiThread {
                recyclerView.adapter = WeightHistoryAdapter(sorted)
            }
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val email = requireContext().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
                .getString("email", "") ?: ""
            val db = ApplicationController.instance?.appDatabase
            val lastEntry = db?.bmiDao()?.getLastEntry(email)
            lastEntryGlobal = lastEntry

            if (lastEntry != null) {
                val bmi = lastEntry.bmi
                val bmiText = "BMI: %.2f".format(bmi)
                val colorResId = when {
                    bmi < 18.5 -> R.color.color1
                    bmi < 25 -> R.color.color5
                    else -> R.color.color1
                }
                val resolvedColor = ContextCompat.getColor(requireContext(), colorResId)
                caloriesRecommended = lastEntry.calories

                requireActivity().runOnUiThread {
                    textViewBmi.text = bmiText
                    textViewBmi.setTextColor(resolvedColor)
                    textViewCalories.text = "Calorii recomandate: %.0f kcal/zi".format(caloriesRecommended)
                }
            } else {
                requireActivity().runOnUiThread {
                    textViewBmi.text = "BMI: necunoscut"
                    textViewCalories.text = "Nu există date disponibile."
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Water Tracker"
            val descriptionText = "Notificări pentru progresul consumului de apă"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("WATER_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
