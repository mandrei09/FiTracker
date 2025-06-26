package com.master.fitnessjourney.fragments

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.switchmaterial.SwitchMaterial
import com.master.fitnessjourney.R
import com.master.fitnessjourney.entities.DifficultyExercicesEnum
import com.master.fitnessjourney.entities.Exercice
import com.master.fitnessjourney.entities.MuscleExercicesEnum
import com.master.fitnessjourney.entities.TypeExercicesEnum
import com.master.fitnessjourney.helpers.Theme
import com.master.fitnessjourney.helpers.extensions.logErrorMessage
import com.master.fitnessjourney.models.CountDiffExcModel
import com.master.fitnessjourney.models.CountTypeExcModel
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.repository.ExcProgressRepository
import com.master.fitnessjourney.repository.ExercicesRepository
import org.jetbrains.annotations.Async.Execute

class HomeFragment : Fragment() {

    private lateinit var themePreferences: Theme
    lateinit var themeSwitch: SwitchMaterial
    private lateinit var chart: BarChart
    private lateinit var chartData: BarData
    private lateinit var dataSet: BarDataSet
    private lateinit var entryList: ArrayList<BarEntry>
    var cardioExcDoneNumber = 0
    var olympicWeightExcDoneNumber = 0
    var plyometricsExcDoneNumber = 0
    var powerliftExcDoneNumber = 0
    var strengthExcDoneNumber = 0
    var stretchExcDoneNumber = 0
    var strongmanExcDoneNumber = 0

    private lateinit var chartPie: PieChart
    private lateinit var chartDataPie: PieData
    private lateinit var dataSetPie: PieDataSet
    private lateinit var entryListPie: ArrayList<PieEntry>
    var beginnerExcNr = 0
    var interExcNr = 0
    var expertExcNr = 0


    private val items = ArrayList<CountTypeExcModel>()
    private val itemsDiff = ArrayList<CountDiffExcModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart = view.findViewById(R.id.bar_chart_exc_type)
        chart.visibility = View.GONE

        chartPie = view.findViewById(R.id.pie_chart_exc_diff)
        chartPie.visibility = View.GONE

        themeSwitch = view.findViewById<SwitchMaterial>(R.id.themeSwitch)
        themePreferences = Theme(requireContext())
        themeSwitch.isChecked = themePreferences.isDarkTheme()
        loadSwitchTheme()
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleThemeSwitch(isChecked)
        }
        showExercisesTypeDistribution()
        showExercicesDiffDistribution()
    }
    private fun showExercicesDiffDistribution() {
        ExcProgressRepository.getCountExcProgressGroupDiff() { exercices ->
            itemsDiff.clear()
            itemsDiff.addAll(exercices)
            for (ex in exercices) {
                when (ex.difficulty.toString()) {
                    "BEGINNER" -> beginnerExcNr += ex.countExcProgress
                    "INTERMEDIATE" -> interExcNr += ex.countExcProgress
                     else -> expertExcNr+= ex.countExcProgress
                }
            }
            chartPieDataUpload()
        }
    }

    private fun showExercisesTypeDistribution() {
        ExcProgressRepository.getCountExcProgressGroupType() { exercices ->
            items.clear()
            items.addAll(exercices)
            for (ex in exercices) {
                when (ex.typeExc.toString()) {
                    "CARDIO" -> cardioExcDoneNumber += ex.countExcProgress
                    "OLYMPIC_WEIGHTLIFTING" -> olympicWeightExcDoneNumber += ex.countExcProgress
                    "PLYOMETRICS" -> plyometricsExcDoneNumber+= ex.countExcProgress
                    "POWERLIFTING" -> powerliftExcDoneNumber+= ex.countExcProgress
                    "STRENGTH" -> strengthExcDoneNumber+= ex.countExcProgress
                    "STRETCHING" -> stretchExcDoneNumber+= ex.countExcProgress
                    else -> strongmanExcDoneNumber+= ex.countExcProgress
                }
            }
            chartDataUpload()
        }
    }
    private fun chartPieDataUpload() {
        getPieChart()
        chartPie.visibility = View.VISIBLE //show when data is ready
    }
    private fun chartDataUpload() {
        getBarsChart()
        chart.visibility = View.VISIBLE //show when data is ready
    }

    fun getBarsChart(){
        entryList = ArrayList()
        entryList.add(BarEntry(1f,cardioExcDoneNumber.toFloat()))
        entryList.add(BarEntry(2f,olympicWeightExcDoneNumber.toFloat()))
        entryList.add(BarEntry(3f,plyometricsExcDoneNumber.toFloat()))
        entryList.add(BarEntry(4f,powerliftExcDoneNumber.toFloat()))
        entryList.add(BarEntry(5f,strengthExcDoneNumber.toFloat()))
        entryList.add(BarEntry(6f,stretchExcDoneNumber.toFloat()))
        entryList.add(BarEntry(7f,strongmanExcDoneNumber.toFloat()))

        dataSet = BarDataSet(entryList, "type count")
        chartData = BarData(dataSet)
        chart.data = chartData
        val colors = listOf(
            resources.getColor(R.color.teal_700), // Color for cardio
            resources.getColor(R.color.purple_200), // Color for olympic
            resources.getColor(R.color.light_primary), // Color for plyometric
            resources.getColor(R.color.teal_200), // Color for powerlift
            resources.getColor(R.color.purple_500), // Color for strength
            resources.getColor(R.color.night_primary), // Color for stretch
            resources.getColor(R.color.night_fields_container), // Color for strongman
        )

        dataSet.colors = colors
        dataSet.valueTextColor = Color.GRAY
        dataSet.valueTextSize = 13f
        chart.description.isEnabled = false

        val legendEntries = mutableListOf<LegendEntry>(
            LegendEntry("Cardio", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.teal_700)),
            LegendEntry("Weighlift", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.purple_200)),
            LegendEntry("Plyometrics", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.light_primary)),
            LegendEntry("Powerlift", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.teal_200)),
            LegendEntry("Strength", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.purple_500)),
            LegendEntry("Stretch", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.night_primary)),
            LegendEntry("Strongman", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.night_fields_container))
        )

        val legend = chart.legend
        legend.textColor = Color.GRAY
        legend.isEnabled = true
        legend.textSize = 13.0F
        legend.setCustom(legendEntries)
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.setDrawInside(false)

        chart.invalidate() // Refresh the chart
    }
    fun getPieChart(){
        entryListPie = ArrayList()
        val sum = beginnerExcNr + interExcNr + expertExcNr
        entryListPie.add(PieEntry(beginnerExcNr.toFloat()/sum *100))
        entryListPie.add(PieEntry(interExcNr.toFloat()/sum *100))
        entryListPie.add(PieEntry(expertExcNr.toFloat()/sum *100))

        dataSetPie = PieDataSet(entryListPie, "type count")
        chartDataPie = PieData(dataSetPie)
        chartPie.data = chartDataPie
        val colors = listOf(
            resources.getColor(R.color.teal_700), // Color for cardio
            resources.getColor(R.color.purple_200), // Color for olympic
            resources.getColor(R.color.light_primary), // Color for plyometric
        )

        dataSetPie.colors = colors
        dataSetPie.valueTextColor = Color.WHITE
        dataSetPie.valueTextSize = 13f
        chartPie.description.isEnabled = false

        val legendEntries = mutableListOf<LegendEntry>(
            LegendEntry("Beginner", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.teal_700)),
            LegendEntry("Intermediate", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.purple_200)),
            LegendEntry("Expert", Legend.LegendForm.SQUARE, 10f, 2f, null, resources.getColor(R.color.light_primary)),
        )

        val legend = chartPie.legend
        legend.textColor = Color.GRAY
        legend.isEnabled = true
        legend.textSize = 13.0F
        legend.setCustom(legendEntries)

        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        chartPie.invalidate() // Refresh the chart
    }
    private fun handleThemeSwitch(isChecked: Boolean) {
        themePreferences.saveTheme(isChecked)
        loadSwitchTheme()
    }

    private fun loadSwitchTheme() {
        themePreferences.loadTheme()
    }

}