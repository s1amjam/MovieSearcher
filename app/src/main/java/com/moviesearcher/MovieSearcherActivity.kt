package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.moviesearcher.databinding.ActivityMovieSearcherBinding
import com.moviesearcher.utils.EncryptedSharedPrefs

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
//                R.id.login_button -> {
//                    AuthorizationDialogFragment().show(
//                        supportFragmentManager, AuthorizationDialogFragment.TAG
//                    )
//
//                    supportFragmentManager.setFragmentResultListener(
//                        "accountResponse",
//                        this
//                    ) { _, bundle ->
//                        val sessionId = bundle.getString("sessionId")
//                        val avatar = bundle.getString("avatar")
//                        val accountId = bundle.getLong("accountId")
//                        val username = bundle.getString("username")
//                        val includeAdult = bundle.getString("includeAdult")
//                        val name = bundle.getString("name")
//
//                        if (sessionId != null || sessionId != "") {
//                            this.sessionId = sessionId.toString()
//
//                            with(encryptedSharedPrefs.edit()) {
//                                putString("sessionId", sessionId)
//                                putString("avatar", avatar)
//                                putLong("accountId", accountId)
//                                putString("username", username)
//                                putString("includeAdult", includeAdult.toString())
//                                putString("name", name)
//                                apply()
//                            }
//                            changeMenu()
//                        }
//                    }
//                    true
//                }
//                R.id.logout_button -> {
//                    //progressBar.visibility = VISIBLE
//
//                    Api.deleteSession(SessionId(sessionId)).observe(this,
//                        { response ->
//                            if (response.success == true) {
//                                encryptedSharedPrefs.edit().clear().apply()
//                                sessionId = ""
//                                changeMenu()
//                                //progressBar.visibility = View.GONE
//                                navController.navigate(R.id.movie_searcher_fragment)
//                            }
//                        })
//                    true
//                }
//                R.id.menu_item_my_lists -> {
//                    navController.navigate(R.id.fragment_my_lists)
//
//                    true
//                }
//                R.id.menu_item_favorites -> {
//                    navController.navigate(R.id.fragment_favorites)
//
//                    true
//                }
                R.id.home -> {
                    navController.navigate(R.id.movie_searcher_fragment)

                    true
                }
                R.id.search -> {
                    navController.navigate(R.id.search_result_fragment)

                    true
                }
                R.id.you -> {
                    TODO()
//                    navController.navigate(R.id.fragment_rated)
//
//                    true
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
        val navController = findNavController(R.id.nav_host_container)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}