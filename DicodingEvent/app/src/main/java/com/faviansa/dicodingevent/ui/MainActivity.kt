package com.faviansa.dicodingevent.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.faviansa.dicodingevent.R
import com.faviansa.dicodingevent.databinding.ActivityMainBinding
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(
            applicationContext,
            SettingPreferences.getInstance(applicationContext.dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.bottomNavView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) else setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}