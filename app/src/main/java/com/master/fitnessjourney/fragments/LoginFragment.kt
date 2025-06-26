package com.master.fitnessjourney.fragments

import android.content.Context
import android.content.SharedPreferences
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
import com.master.fitnessjourney.helpers.LogInOutEvent
import com.master.fitnessjourney.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

class LoginFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences =
            requireActivity().getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val doLoginBtn = view.findViewById<Button>(R.id.button_login)
        doLoginBtn.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin() {
        val usernameInput = view?.findViewById<TextInputLayout>(R.id.et_username)
        val passwordInput = view?.findViewById<TextInputLayout>(R.id.et_password)

        val username = usernameInput?.editText?.text.toString().trim()
        val password = passwordInput?.editText?.text.toString()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Completează toate câmpurile", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val user = UserRepository.login(username, password)

            withContext(Dispatchers.Main) {
                if (user != null) {
                    sharedPreferences.edit().putString("email", user.email).apply()

                    Toast.makeText(requireContext(), "Autentificare reușită!", Toast.LENGTH_SHORT).show()

                    EventBus.getDefault().post(LogInOutEvent(isLoggedIn = true))
                    goToHome()
                } else {
                    Toast.makeText(requireContext(), "Utilizator sau parolă incorectă", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToHome() {
        val action = LoginFragmentDirections.actionNavigationLoginToNavigationHome()
        findNavController().navigate(action)
    }
}
