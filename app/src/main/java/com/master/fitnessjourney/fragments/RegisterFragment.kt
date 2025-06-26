package com.master.fitnessjourney.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.master.fitnessjourney.R
import com.master.fitnessjourney.entities.UserEntity
import com.master.fitnessjourney.helpers.LogInOutEvent
import com.master.fitnessjourney.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

class RegisterFragment : Fragment() {

    private lateinit var emailInput: TextInputLayout
    private lateinit var usernameInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout
    private lateinit var confirmPasswordInput: TextInputLayout
    private lateinit var registerButton: Button
    private lateinit var firstNameInput: TextInputLayout
    private lateinit var lastNameInput: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailInput = view.findViewById(R.id.email)
        usernameInput = view.findViewById(R.id.username)
        passwordInput = view.findViewById(R.id.password)
        confirmPasswordInput = view.findViewById(R.id.confirmPassword)
        registerButton = view.findViewById(R.id.button_signup)
        firstNameInput = view.findViewById(R.id.firstName)
        lastNameInput = view.findViewById(R.id.lastName)


        registerButton.setOnClickListener {
            handleRegister()
        }

        return view
    }

    private fun handleRegister() {
        val email = emailInput.editText?.text.toString().trim()
        val username = usernameInput.editText?.text.toString().trim()
        val password = passwordInput.editText?.text.toString()
        val confirmPassword = confirmPasswordInput.editText?.text.toString()
        val firstName = firstNameInput.editText?.text.toString().trim()
        val lastName = lastNameInput.editText?.text.toString().trim()

        if (firstName.isBlank() || lastName.isBlank() ||
            email.isBlank() || username.isBlank() || password.isBlank()) {
            Toast.makeText(requireContext(), "Completează toate câmpurile", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            confirmPasswordInput.error = "Parolele nu coincid"
            return
        }

        val user = UserEntity(
            email = email,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val existingUser = UserRepository.getByEmail(email)

            if (existingUser != null) {
                withContext(Dispatchers.Main) {
                    emailInput.error = "Există deja un utilizator înregistrat cu acest email"
                    Toast.makeText(requireContext(), "Înregistrare eșuată", Toast.LENGTH_SHORT).show()
                }
            } else {
                UserRepository.register(user)

                val prefs = requireContext().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
                prefs.edit().putString("email", email).apply()

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Înregistrare reușită!", Toast.LENGTH_SHORT).show()
                    EventBus.getDefault().post(LogInOutEvent(isLoggedIn = true))
                    findNavController().navigate(R.id.navigation_home)
                }
            }
        }


    }
}
