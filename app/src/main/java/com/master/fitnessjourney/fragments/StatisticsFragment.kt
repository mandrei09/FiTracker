package com.master.fitnessjourney.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.textfield.TextInputLayout
import com.master.fitnessjourney.R
import com.master.fitnessjourney.adapters.ExerciceDoneAdaptor
import com.master.fitnessjourney.adapters.ExerciceListAdaptor
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.repository.ExcProgressRepository
import com.master.fitnessjourney.repository.ExercicesRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class StatisticsFragment : Fragment() {
    var calendar = Calendar.getInstance()
    private val items = ArrayList<ExerciceModel>()
    private val adapter = ExerciceDoneAdaptor(items)
    private lateinit var sharedPreferences: SharedPreferences
    var beginnerExcDoneNumber = 0
    var intermediateExcDoneNumber =  0
    var expertExcDoneNumber = 0


    private lateinit var chart: BarChart
    private lateinit var chartData: BarData
    private lateinit var dataSet: BarDataSet
    private lateinit var entryList: ArrayList<BarEntry>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences =
            (activity?.getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE))!!
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = sharedPreferences.getString("email",null)
        chart = view?.findViewById(R.id.barChart)!!
        getBarsChart()
        setupRecyclerView()
        val button = view?.findViewById<Button>(R.id.button_select_date)

        val setDate = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(viewDate: DatePicker?, year: Int, month: Int, day: Int) {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                DateUpdate()
                val dataText = view?.findViewById<TextView>(R.id.tv_date_choosed)?.text
                if(dataText!=null && username != null) {
                    val parts = dataText.split("/")
                    val day_str = parts[0]
                    val month_str = parts[1]
                    val year_str = parts[2]
                    beginnerExcDoneNumber = 0
                    intermediateExcDoneNumber = 0
                    expertExcDoneNumber = 0
                    ExercicesRepository.getExcDoneByDateUsername(day_str,month_str,year_str,username) { exercices ->
                        items.clear()
                        items.addAll(exercices)
                        for (ex in exercices){
                            if(ex.difficulty == "BEGINNER"){
                                beginnerExcDoneNumber = beginnerExcDoneNumber + 1
                            }
                            else if(ex.difficulty == "INTERMEDIATE"){
                                intermediateExcDoneNumber = intermediateExcDoneNumber +1
                            }
                            else
                                expertExcDoneNumber = expertExcDoneNumber + 1
                        }

                        adapter.notifyDataSetChanged()
                        getBarsChart()
                        chart.visibility = View.VISIBLE
                    }

                }
            }
        }
        adapter.notifyDataSetChanged()
        button?.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                setDate,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun DateUpdate() {
        val customFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(customFormat, Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("Europe/Bucharest")
        val textView = view?.findViewById<TextView>(R.id.tv_date_choosed)
        if (textView != null) {
            textView.text = sdf.format(calendar.time)
        }
    }
    fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        val rvExercices = view?.findViewById<RecyclerView>(R.id.rv_exercices_done) ?: return
        rvExercices.apply {

            this.layoutManager = layoutManager
            this.adapter = this@StatisticsFragment.adapter
        }
    }

    fun getBarsChart(){
        entryList = ArrayList()
        entryList.add(BarEntry(1f,beginnerExcDoneNumber.toFloat()))
        entryList.add(BarEntry(2f,intermediateExcDoneNumber.toFloat()))
        entryList.add(BarEntry(3f,expertExcDoneNumber.toFloat()))

        dataSet = BarDataSet(entryList, "difficulty count")
        chartData = BarData(dataSet)
        chart.data = chartData
        val colors = listOf(
            resources.getColor(R.color.teal_700), // Color for Intermediate
            resources.getColor(R.color.purple_200), // Color for Medium
            resources.getColor(R.color.light_primary) // Color for Hard
        )

        // Apply colors to dataset
        dataSet.colors = colors
        dataSet.valueTextColor = Color.GRAY
        dataSet.valueTextSize = 11f
        chart.description.isEnabled = false

        val legendEntries = mutableListOf<LegendEntry>(
            LegendEntry("Beginner", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.teal_700)),
            LegendEntry("Intermediate", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.purple_200)),
            LegendEntry("Expert", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.light_primary))
        )

        val legend = chart.legend
        legend.isEnabled = true
        legend.textSize = 16.0F
        legend.textColor = Color.GRAY
        legend.setCustom(legendEntries)

        chart.invalidate() // Refresh the chart
    }
}