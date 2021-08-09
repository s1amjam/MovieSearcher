package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.moviesearcher.api.Api
import com.moviesearcher.common.AuthorizationDialogFragment
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.databinding.ActivityMovieSearcherBinding
import com.moviesearcher.utils.EncryptedSharedPrefs

private const val TAG = "MovieSearcherActivity"

class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMovieSearcherBinding

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var sessionId: String
    private val isLoggedIn: String get() = sessionId

    private lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieSearcherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        drawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        val progressBar = binding.progressBarActivityMovieSearcher

        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(applicationContext)
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()

        setSupportActionBar(toolbar)
        setupNavigation()
        setupAppBarConfig()
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_button -> {
                    AuthorizationDialogFragment().show(
                        supportFragmentManager, AuthorizationDialogFragment.TAG
                    )

                    supportFragmentManager.setFragmentResultListener(
                        "accountResponse",
                        this
                    ) { _, bundle ->
                        val sessionId = bundle.getString("sessionId")
                        val avatar = bundle.getString("avatar")
                        val accountId = bundle.getLong("accountId")
                        val username = bundle.getString("username")
                        val includeAdult = bundle.getString("includeAdult")
                        val name = bundle.getString("name")

                        if (sessionId != null || sessionId != "") {
                            this.sessionId = sessionId.toString()

                            with(encryptedSharedPrefs.edit()) {
                                putString("sessionId", sessionId)
                                putString("avatar", avatar)
                                putLong("accountId", accountId)
                                putString("username", username)
                                putString("includeAdult", includeAdult.toString())
                                putString("name", name)
                                apply()
                            }
                            changeMenu()
                        }
                    }
                    true
                }
                R.id.logout_button -> {
                    progressBar.visibility = VISIBLE

                    Api.deleteSession(SessionId(sessionId)).observe(this,
                        { response ->
                            if (response.success == true) {
                                encryptedSharedPrefs.edit().clear().apply()
                                sessionId = ""
                                changeMenu()
                                progressBar.visibility = View.GONE
                                navController.navigate(R.id.movie_searcher_fragment)
                            }
                        })
                    true
                }
                R.id.menu_item_my_lists -> {
                    drawerLayout.close()
                    navController.navigate(R.id.fragment_my_lists)

                    true
                }
                R.id.menu_item_favorites -> {
                    drawerLayout.close()
                    navController.navigate(R.id.fragment_favorites)

                    true
                }
                R.id.menu_item_ratings -> {
                    drawerLayout.close()
                    navController.navigate(R.id.fragment_rated)

                    true
                }
                R.id.menu_item_watchlists -> {
                    drawerLayout.close()
                    navController.navigate(R.id.fragment_watchlist)

                    true
                }
                R.id.menu_item_trending -> {
                    drawerLayout.close()
                    navController.navigate(R.id.movie_searcher_fragment)

                    true
                }
                else -> super.onOptionsItemSelected(menuItem)
            }
        }
    }

    private fun setupAppBarConfig() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movie_info_fragment,
                R.id.movie_searcher_fragment,
                R.id.tv_info_fragment,
                R.id.search_result_fragment,
                R.id.fragment_my_lists,
                R.id.fragment_my_list,
                R.id.fragment_favorites,
                R.id.fragment_rated,
                R.id.fragment_watchlist
            ), drawerLayout
        )
    }

    private fun changeMenu() {
        navigationView.invalidate()
        drawerLayout.close()
        navigationView.menu.clear()
        inflateMenuWithAuthorization()
    }

    private fun setupNavigation() {
        navController = binding.navHostContainer.getFragment<NavHostFragment>()?.navController!!
        navigationView = binding.navView
        navigationView.setupWithNavController(navController)
        inflateMenuWithAuthorization()
    }

    private fun inflateMenuWithAuthorization() {
        if (isLoggedIn.isNotBlank()) {
            navigationView.inflateMenu(R.menu.navigation_drawer_menu_with_login)
        } else {
            navigationView.inflateMenu(R.menu.navigation_drawer_menu_with_logout)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_container)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}