package com.master.fitnessjourney.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.master.fitnessjourney.R
import com.master.fitnessjourney.adapters.ExerciceProgressAdaptor
import com.master.fitnessjourney.models.ExerciceInProgress
import com.master.fitnessjourney.repository.ExcProgressRepository

class InProgressExercicesFragment : Fragment() {
    private val items = ArrayList<ExerciceInProgress>()
    private lateinit var adapter: ExerciceProgressAdaptor
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = requireActivity()
            .getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_in_progress_exercices, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ExerciceProgressAdaptor(
            items,
            { exc -> deleteItem(exc) },
            { exc -> updateItem(exc) },
            { videoUrl -> showVideoPopup(videoUrl) } 
        )

        setupRecyclerView()
        sharedPreferences.getString("email", null)?.let {
            showData(it)
        }

        val statisticsBtn = view.findViewById<Button>(R.id.btn_statistics)
        statisticsBtn.setOnClickListener {
            findNavController().navigate(R.id.navigation_statistics)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        val rvExercicesProgress =
            view?.findViewById<RecyclerView>(R.id.rv_exercices_progress) ?: return

        rvExercicesProgress.apply {
            this.layoutManager = layoutManager
            this.adapter = this@InProgressExercicesFragment.adapter
        }
    }

    private fun showData(username: String) {
        ExcProgressRepository.getExcProgress(username) { exercices ->
            items.clear()
            items.addAll(exercices)
            adapter.notifyDataSetChanged()
            if (items.isEmpty()) {
                Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
            }
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
        sharedPreferences.getString("email", null)?.let { showData(it) }
    }

    private fun showVideoPopup(videoName: String) {
        if (videoName.isBlank()) {
            Toast.makeText(requireContext(), "No video available for this exercise.", Toast.LENGTH_SHORT).show()
            return
        }

        val dialogView = layoutInflater.inflate(R.layout.dialog_video_player, null)
        val videoView = dialogView.findViewById<VideoView>(R.id.video_view)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/raw/$videoName")
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { videoView.start() }
        dialog.show()
    }

}