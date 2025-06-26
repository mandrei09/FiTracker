package com.master.fitnessjourney.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.master.fitnessjourney.R
import com.master.fitnessjourney.adapters.ExerciceListAdaptor
import com.master.fitnessjourney.adapters.ExerciceProgressAdaptor
import com.master.fitnessjourney.helpers.Theme
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.repository.ExcProgressRepository
import java.util.prefs.Preferences

class InProgressExercicesFragment : Fragment() {
    private val items = ArrayList<ExerciceInProgress>()
    private val adapter = ExerciceProgressAdaptor(items,
        {exc -> deleteItem(exc)},
        {exc -> updateItem(exc)})
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences =
            (activity?.getSharedPreferences("CONTEXT_DETAILS",Context.MODE_PRIVATE))!!
        return inflater.inflate(R.layout.fragment_in_progress_exercices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = sharedPreferences.getString("email",null)

        setupRecyclerView()
        if(username!=null)
            {showData(username)}
    }
    private fun showData(username: String) {
        ExcProgressRepository.getExcProgress(username) { exercices ->
            items.clear()
            items.addAll(exercices)
            adapter.notifyDataSetChanged()
            if(items.isEmpty()){
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun setupRecyclerView(){
        val layoutManager = LinearLayoutManager(context)
        val rvExercicesProgress = view?.findViewById<RecyclerView>(R.id.rv_exercices_progress) ?: return

        rvExercicesProgress.apply {
            this.layoutManager = layoutManager
            this.adapter = this@InProgressExercicesFragment.adapter
        }
    }
    private fun deleteItem(exercice: ExerciceInProgress) {
        ExcProgressRepository.deleteExerciceInProgress(exercice) {
            adapter.removeItem(exercice)
            adapter.notifyDataSetChanged()
        }
    }

    private fun updateItem(exercice: ExerciceInProgress) {
        ExcProgressRepository.updateExerciceInProgress(exercice) {
            adapter.updateItem(exercice)
        }
        sharedPreferences.getString("email",null)?.let { showData(it) }
    }
}