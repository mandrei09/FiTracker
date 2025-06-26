package com.master.fitnessjourney.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
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
import com.master.fitnessjourney.helpers.Theme
import com.master.fitnessjourney.models.CountDiffExcModel
import com.master.fitnessjourney.models.CountTypeExcModel
import com.master.fitnessjourney.repository.ExcProgressRepository

class HomeFragment : Fragment() {

    private lateinit var themePreferences: Theme
    lateinit var themeSwitch: SwitchMaterial
    private lateinit var chart: HorizontalBarChart
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
    private val currentUsername: String?
        get() = activity?.getSharedPreferences(
            "CONTEXT_DETAILS",
            android.content.Context.MODE_PRIVATE
        )?.getString("email", null)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chart = view.findViewById(R.id.bar_chart_exc_type)
        chart.visibility = View.GONE

        chartPie = view.findViewById(R.id.pie_chart_exc_diff)
        chartPie.visibility = View.GONE

        themeSwitch = view.findViewById(R.id.themeSwitch)
        themePreferences = Theme(requireContext())
        themeSwitch.isChecked = themePreferences.isDarkTheme()
        loadSwitchTheme()

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            handleThemeSwitch(isChecked)
        }

        if (currentUsername != null) {
            showExercisesTypeDistribution(currentUsername!!)
            showExercicesDiffDistribution(currentUsername!!)
        }
    }

    private fun showExercicesDiffDistribution(username: String) {
        ExcProgressRepository.getCountExcProgressGroupDiff(username) { exercices ->
            beginnerExcNr = 0
            interExcNr = 0
            expertExcNr = 0
            itemsDiff.clear()
            itemsDiff.addAll(exercices)
            for (ex in exercices) {
                when (ex.difficulty.toString()) {
                    "BEGINNER" -> beginnerExcNr += ex.countExcProgress
                    "INTERMEDIATE" -> interExcNr += ex.countExcProgress
                    else -> expertExcNr += ex.countExcProgress
                }
            }
            chartPieDataUpload()
        }
    }

    private fun showExercisesTypeDistribution(username: String) {
        ExcProgressRepository.getCountExcProgressGroupType(username) { exercices ->
            cardioExcDoneNumber = 0
            olympicWeightExcDoneNumber = 0
            plyometricsExcDoneNumber = 0
            powerliftExcDoneNumber = 0
            strengthExcDoneNumber = 0
            stretchExcDoneNumber = 0
            strongmanExcDoneNumber = 0

            items.clear()
            items.addAll(exercices)
            for (ex in exercices) {
                when (ex.typeExc.toString()) {
                    "CARDIO" -> cardioExcDoneNumber += ex.countExcProgress
                    "OLYMPIC_WEIGHTLIFTING" -> olympicWeightExcDoneNumber += ex.countExcProgress
                    "PLYOMETRICS" -> plyometricsExcDoneNumber += ex.countExcProgress
                    "POWERLIFTING" -> powerliftExcDoneNumber += ex.countExcProgress
                    "STRENGTH" -> strengthExcDoneNumber += ex.countExcProgress
                    "STRETCHING" -> stretchExcDoneNumber += ex.countExcProgress
                    else -> strongmanExcDoneNumber += ex.countExcProgress
                }
            }
            chartDataUpload()
        }
    }


    private fun chartDataUpload() {
        if (!isAdded || context == null) return
        getBarsChart()
        chart.visibility = View.VISIBLE
    }

    private fun chartPieDataUpload() {
        if (!isAdded || context == null) return
        getPieChart()
        chartPie.visibility = View.VISIBLE
    }

    fun getBarsChart() {
        if (!isAdded || context == null) return
        entryList = ArrayList()
        entryList.add(BarEntry(1f, cardioExcDoneNumber.toFloat()))
        entryList.add(BarEntry(2f, olympicWeightExcDoneNumber.toFloat()))
        entryList.add(BarEntry(3f, plyometricsExcDoneNumber.toFloat()))
        entryList.add(BarEntry(4f, powerliftExcDoneNumber.toFloat()))
        entryList.add(BarEntry(5f, strengthExcDoneNumber.toFloat()))
        entryList.add(BarEntry(6f, stretchExcDoneNumber.toFloat()))
        entryList.add(BarEntry(7f, strongmanExcDoneNumber.toFloat()))

        dataSet = BarDataSet(entryList, "type count")
        chartData = BarData(dataSet)
        chart.data = chartData

        val colors = listOf(
            requireContext().getColor(R.color.color1),
            requireContext().getColor(R.color.color2),
            requireContext().getColor(R.color.color3),
            requireContext().getColor(R.color.color4),
            requireContext().getColor(R.color.color5),
            requireContext().getColor(R.color.color6),
            requireContext().getColor(R.color.color7),
        )
        dataSet.colors = colors
        dataSet.valueTextColor = Color.GRAY
        dataSet.valueTextSize = 13f
        chart.description.isEnabled = false

        val legendEntries = listOf(
            LegendEntry("Cardio", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[0]),
            LegendEntry("Weighlift", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[1]),
            LegendEntry("Plyometrics", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[2]),
            LegendEntry("Powerlift", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[3]),
            LegendEntry("Strength", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[4]),
            LegendEntry("Stretch", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[5]),
            LegendEntry("Strongman", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[6])
        )

        chart.legend.apply {
            isWordWrapEnabled = true
            form = Legend.LegendForm.SQUARE
            textColor = Color.GRAY
            isEnabled = true
            textSize = 13.0F
            setCustom(legendEntries)
            verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            orientation = Legend.LegendOrientation.HORIZONTAL
            chart.axisLeft.isEnabled = false
            chart.axisRight.isEnabled = true
            chart.xAxis.apply {
                position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
            }
            setDrawInside(false)
            xEntrySpace = 10f  // space between items
            yEntrySpace = 5f
        }
        chart.extraBottomOffset = 24f
        chart.invalidate()
    }

    fun getPieChart() {
        if (!isAdded || context == null) return
        entryListPie = ArrayList()
        val sum = beginnerExcNr + interExcNr + expertExcNr
        if (sum == 0) return

        entryListPie.add(PieEntry(beginnerExcNr.toFloat() / sum * 100))
        entryListPie.add(PieEntry(interExcNr.toFloat() / sum * 100))
        entryListPie.add(PieEntry(expertExcNr.toFloat() / sum * 100))

        dataSetPie = PieDataSet(entryListPie, "type count")
        chartDataPie = PieData(dataSetPie)
        chartPie.data = chartDataPie

        val colors = listOf(
            requireContext().getColor(R.color.color5),
            requireContext().getColor(R.color.color6),
            requireContext().getColor(R.color.color1),
        )

        dataSetPie.colors = colors
        dataSetPie.valueTextColor = Color.WHITE
        dataSetPie.valueTextSize = 13f
        chartPie.description.isEnabled = false

        val legendEntries = listOf(
            LegendEntry("Beginner", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[0]),
            LegendEntry("Intermediate", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[1]),
            LegendEntry("Expert", Legend.LegendForm.SQUARE, 10f, 2f, null, colors[2])
        )

        chartPie.legend.apply {
            textColor = Color.GRAY
            isEnabled = true
            textSize = 13.0F
            setCustom(legendEntries)
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
        }

        chartPie.invalidate()
    }

    private fun handleThemeSwitch(isChecked: Boolean) {
        themePreferences.saveTheme(isChecked)
        loadSwitchTheme()
    }

    private fun loadSwitchTheme() {
        themePreferences.loadTheme()
    }
}
