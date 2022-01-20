package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moviesearcher.databinding.ActivityMovieSearcherBinding
import com.moviesearcher.common.utils.EncryptedSharedPrefs

private const val TAG = "MovieSearcherActivity"

class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieSearcherBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var sessionId: String

    private lateinit var navigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieSearcherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(applicationContext)
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()

        setupNavigation()

        navigationView.setOnNavigationItemSelectedListener { menuItem ->
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
    }

    private fun setupNavigation() {
        navController = binding.navHostContainer.getFragment<NavHostFragment>()?.navController!!
        navigationView = binding.navView
        navigationView.setupWithNavController(navController)
        navigationView.inflateMenu(R.menu.bottom_navigation_menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}