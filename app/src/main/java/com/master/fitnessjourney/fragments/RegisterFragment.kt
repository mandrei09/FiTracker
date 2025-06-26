package com.master.fitnessjourney.fragments

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.VolleyLog.TAG
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputLayout
import com.master.fitnessjourney.BuildConfig
import com.master.fitnessjourney.R
import org.json.JSONObject

class RegisterFragment : Fragment() {
    private lateinit var email: TextInputLayout
    private lateinit var username: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var confirmPassword: TextInputLayout
    private lateinit var signUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        // Initialize views
        email = view.findViewById(R.id.email)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        confirmPassword = view.findViewById(R.id.confirmPassword)
        signUpButton = view.findViewById(R.id.button_signup)

        // Set up text watcher
        val textWatcher = object : TextWatcher {
            // Override methods here
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val emailText = email.editText?.text.toString()
                val usernameText = username.editText?.text.toString()
                val passwordText = password.editText?.text.toString()
                val confirmPasswordText = confirmPassword.editText?.text.toString()

                var pass = true

                if (!confirmPasswordText.isEmpty()) {
                    if (notTheSame(passwordText, confirmPasswordText)) {
                        confirmPassword.error = "Not the same password"
                        pass = false
                    } else {
                        confirmPassword.helperText = "Ok"
                    }
                } else {
                    confirmPassword.helperText = "*Required"
                }

                if (!emailText.isEmpty()) {
                    if (!isValidEmail(emailText)) {
                        email.error = "Invalid format for email"
                        pass = false
                    } else {
                        email.helperText = "Ok"
                    }
                } else {
                    email.helperText = "*Required"
                }

                if (!usernameText.isEmpty()) {
                    username.helperText = "Ok"
                } else {
                    pass = false
                    username.helperText = "*Required"
                }

                if (!passwordText.isEmpty()) {
                    if (!isValidPassword(passwordText)) {
                        password.error = "Must contain lowercase characters, uppercase characters, numbers, and special characters"
                        pass = false
                    } else {
                        password.helperText = "Ok"
                    }
                } else {
                    password.helperText = "*Required"
                }
//
                signUpButton.isEnabled = pass
            }

            override fun afterTextChanged(editable: Editable?) {
                // No action needed
            }
        }

        // Attach text watcher to TextInputLayouts
        email.editText?.addTextChangedListener(textWatcher)
        password.editText?.addTextChangedListener(textWatcher)
        confirmPassword.editText?.addTextChangedListener(textWatcher)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here

            super.onViewCreated(view, savedInstanceState)

            signUpButton.setOnClickListener {
                signUp()
            }
        }

    }

    fun notTheSame(password: String, confirmPassword: String): Boolean {
        return password != confirmPassword
    }

    fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^[a-zA-Z0-9@#$%^&!\\\\+=()*_/].{5,20}$"
        return password.matches(passwordRegex.toRegex())
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        return email.matches(emailRegex.toRegex())
    }

    fun signUp() {
        val emailText = email.editText?.text.toString()
        val usernameText = username.editText?.text.toString()
        val passwordText = password.editText?.text.toString()

        val username: String
        val password: String
        val email: String
        val phone: String
        val firstname: String
        val lastname: String


        when (BuildConfig.DEBUG) {
            true -> {
                username = "johnd"
                password = "Parola123!"
                email="m38rmF\$"
                phone="1-570-236-7033"
                firstname="John"
                lastname="Doe"
            }

            false -> {
                username = usernameText
                password = passwordText
                email = emailText
                phone="1-570-236-7033"
                firstname="John"
                lastname="Doe"
            }
        }

        val url = "${BuildConfig.BASE_URL}users"
        val stringRequest = object : StringRequest(
            Method.POST,
            url,
            { response ->
                Log.d(TAG, response.toString())

                Toast.makeText(activity,  "Signed up with success", Toast.LENGTH_LONG).show();
                signUpButton.isEnabled = false
            },
            { error ->
                Log.e(error.toString(), this.toString())

                Toast.makeText(activity, "That didn't work!", Toast.LENGTH_LONG).show();
                Toast.makeText(activity, "Error: " + error.message, Toast.LENGTH_LONG).show();
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params: MutableMap<String, String> = HashMap()

                // Fill in the simple fields directly
                params["username"] = username
                params["password"] = password
                params["email"] = email
                params["phone"] = phone

                // Create nested JSON objects for name and address
                val nameObject = JSONObject()
                nameObject.put("firstname", firstname)
                nameObject.put("lastname", lastname)

                // Put the nested JSON objects into the params map as strings
                params["name"] = nameObject.toString()

                return params
            }
        }

        stringRequest.tag = "SRTAG"

        Volley.newRequestQueue(requireContext()).add(stringRequest)
    }
}