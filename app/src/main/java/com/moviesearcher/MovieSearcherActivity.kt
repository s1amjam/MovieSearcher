package com.moviesearcher

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moviesearcher.common.utils.Constants.DARK_MODE
import com.moviesearcher.databinding.ActivityMovieSearcherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieSearcherBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var navigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMovieSearcherBinding.inflate(layoutInflater)
        val view = binding.root
        val prefs = getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE)
        val mode = prefs.getInt(DARK_MODE, 1)
        setContentView(view)

        setupNavigation()

        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.home_fragment)

                    true
                }
                R.id.search -> {
                    navController.navigate(R.id.search_result_fragment)

                    true
                }
                R.id.you -> {
                    navController.navigate(R.id.you_fragment)

                    true
                }
                else -> super.onOptionsItemSelected(menuItem)
            }
        }

        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun setupNavigation() {
        navController = binding.navHostContainer.getFragment<NavHostFragment>().navController
        navigationView = binding.navView
        navigationView.setupWithNavController(navController)
        navigationView.inflateMenu(R.menu.bottom_navigation_menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}