package com.master.fitnessjourney

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.master.fitnessjourney.helpers.LogInOutEvent
import com.master.fitnessjourney.repository.UserRepository
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var sharedPreferences: SharedPreferences
    lateinit var optionsMenu: Menu
    var shouldShowOptionsMenu: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CONTEXT_DETAILS", Context.MODE_PRIVATE)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.mobile_navigation)
        val email = sharedPreferences.getString("email", "")
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        if (email.isNullOrEmpty()) {
            navGraph.setStartDestination(R.id.navigation_login)
            unlogggedBottomNavigation()
        } else {
            navGraph.setStartDestination(R.id.navigation_home)
            logggedBottomNavigation()
        }
        navController.graph = navGraph

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }

                R.id.navigation_login -> {
                    navController.navigate(R.id.navigation_login)
                    true
                }

                R.id.navigation_register -> {
                    navController.navigate(R.id.navigation_register)
                    true
                }

                R.id.navigation_find_exercices -> {
                    navController.navigate(R.id.navigation_find_exercices)
                    true
                }

                R.id.navigation_in_progress_exercices -> {
                    navController.navigate(R.id.navigation_in_progress_exercices)
                    true
                }

                R.id.navigation_diary -> {
                    navController.navigate(R.id.navigation_diary)
                    true
                }

                R.id.navigation_bmi -> {
                    navController.navigate(R.id.navigation_bmi)
                    true
                }

                else -> false
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLogInOutEvent(event: LogInOutEvent) {
        if (event.isLoggedIn)
            logggedBottomNavigation()
        else
            unlogggedBottomNavigation()
    }

    fun logggedBottomNavigation() {
        bottomNavigationView.menu.clear()
        bottomNavigationView.inflateMenu(R.menu.bottom_logged_user_menu)
        toggleOptionsMenuVisibility(true)
    }

    fun unlogggedBottomNavigation() {
        bottomNavigationView.menu.clear()
        bottomNavigationView.inflateMenu(R.menu.bottom_unlogged_user_menu)
        toggleOptionsMenuVisibility(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (shouldShowOptionsMenu) {
            menuInflater.inflate(R.menu.options_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.option_menu_exit -> {
                sharedPreferences.edit().putString("email", "").apply()
                EventBus.getDefault().post(LogInOutEvent(isLoggedIn = false))
                navController.navigate(R.id.navigation_login)
                true
            }

            R.id.option_menu_delete -> {
                showDeleteConfirmationDialog()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Account")
        builder.setMessage("Are you sure you want to delete your account?")
        builder.setPositiveButton("Yes") { _, _ ->
            val email = sharedPreferences.getString("email", null)
            if (!email.isNullOrBlank()) {
                lifecycleScope.launch {
                    UserRepository.deleteByEmail(email)
                    sharedPreferences.edit().putString("email", "").apply()
                    EventBus.getDefault().post(LogInOutEvent(isLoggedIn = false))
                    navController.navigate(R.id.navigation_login)
                    Toast.makeText(this@MainActivity, "Account deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    fun toggleOptionsMenuVisibility(shouldShow: Boolean) {
        shouldShowOptionsMenu = shouldShow
        invalidateOptionsMenu()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}