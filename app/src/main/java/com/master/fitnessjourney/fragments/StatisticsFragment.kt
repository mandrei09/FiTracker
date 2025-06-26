package com.master.fitnessjourney.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.animation.Easing
import com.master.fitnessjourney.R
import com.master.fitnessjourney.adapters.ExerciceDoneAdaptor
import com.master.fitnessjourney.models.ExerciceModel
import com.master.fitnessjourney.repository.ExercicesRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class StatisticsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences
    private val calendar = Calendar.getInstance()

    private lateinit var chartPie: PieChart
    private var beginnerCount = 0
    private var intermediateCount = 0
    private var expertCount = 0

    private val items = ArrayList<ExerciceModel>()
    private val adapter = ExerciceDoneAdaptor(items)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity()
            .getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartPie = view.findViewById(R.id.pie_chart_stats)
        chartPie.visibility = View.GONE

        // RecyclerView setup
        val rv = view.findViewById<RecyclerView>(R.id.rv_exercices_done)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = adapter

        // initial empty chart
        drawPieChart()

        view.findViewById<Button>(R.id.button_select_date).setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _: DatePicker, year, month, day ->
                    calendar.set(year, month, day)
                    updateDateText()
                    fetchDataAndShowChart()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateText() {
        val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fmt.timeZone = TimeZone.getTimeZone("Europe/Bucharest")
        view?.findViewById<TextView>(R.id.tv_date_choosed)?.text = fmt.format(calendar.time)
    }

    private fun fetchDataAndShowChart() {
        val dateStr = view?.findViewById<TextView>(R.id.tv_date_choosed)?.text?.toString()
            ?: return
        val user = sharedPreferences.getString("email", "") ?: return
        val parts = dateStr.split("/")

        beginnerCount = 0
        intermediateCount = 0
        expertCount = 0
        items.clear()

        ExercicesRepository.getExcDoneByDateUsername(
            parts[0], parts[1], parts[2], user
        ) { exercices ->
            items.addAll(exercices)
            exercices.forEach { ex ->
                when (ex.difficulty) {
                    "BEGINNER"     -> beginnerCount++
                    "INTERMEDIATE" -> intermediateCount++
                    else           -> expertCount++
                }
            }
            adapter.notifyDataSetChanged()
            drawPieChart()
            chartPie.visibility = View.VISIBLE
        }
    }

    private fun drawPieChart() {
        val sum = (beginnerCount + intermediateCount + expertCount).takeIf { it > 0 } ?: 1
        val entries = arrayListOf(
            PieEntry(beginnerCount / sum.toFloat() * 100f),
            PieEntry(intermediateCount / sum.toFloat() * 100f),
            PieEntry(expertCount / sum.toFloat() * 100f)
        )

        val dataSet = PieDataSet(entries, "").apply {
            sliceSpace = 2f
            // procentajele vor fi albe
            valueTextColor = R.color.light_fields_container
            valueTextSize = 12f
            colors = listOf(
                ContextCompat.getColor(requireContext(), R.color.color5),
                ContextCompat.getColor(requireContext(), R.color.color6),
                ContextCompat.getColor(requireContext(), R.color.color1)
            )

            setDrawValues(true)
        }

        chartPie.apply {
            data = PieData(dataSet)
            setUsePercentValues(true)
            isDrawHoleEnabled = true
            holeRadius = 50f

            // NU mai afișăm numele categoriei pe felii:
            setDrawEntryLabels(false)

            description.isEnabled = false

            legend.apply {
                isEnabled = true
                textSize = 13f
                form = Legend.LegendForm.SQUARE
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setCustom(arrayListOf(
                    LegendEntry("Beginner", Legend.LegendForm.SQUARE, 10f, 2f, null,
                        ContextCompat.getColor(requireContext(), R.color.color5)),
                    LegendEntry("Intermediate", Legend.LegendForm.SQUARE, 10f, 2f, null,
                        ContextCompat.getColor(requireContext(), R.color.color6)),
                    LegendEntry("Expert", Legend.LegendForm.SQUARE, 10f, 2f, null,
                        ContextCompat.getColor(requireContext(), R.color.color1))
                ))
            }

            animateY(800, Easing.EaseInOutQuad)
            invalidate()
        }
    }

}
