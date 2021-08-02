package com.moviesearcher

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
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
import com.moviesearcher.utils.EncryptedSharedPrefs

private const val TAG = "MovieSearcherActivity"

//TODO: remove everything to a separate class?
class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sessionId: String
    private lateinit var loginButton: MenuItem
    private lateinit var logoutButton: MenuItem
    private lateinit var myListsMenuItem: MenuItem
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var favoritesMenuItem: MenuItem
    private lateinit var ratingsMenuItem: MenuItem
    private lateinit var watchlistMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_searcher)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_activity_movie_searcher)

        encryptedSharedPrefs = EncryptedSharedPrefs.sharedPrefs(applicationContext)
        sessionId = encryptedSharedPrefs.getString("sessionId", "").toString()

        navigationView.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        navigationView.inflateMenu(R.menu.navigation_drawer_menu)
        val menu = navigationView.menu

        loginButton = menu.findItem(R.id.login_button)
        logoutButton = menu.findItem(R.id.logout_button)
        myListsMenuItem = menu.findItem(R.id.menu_item_my_lists)
        favoritesMenuItem = menu.findItem(R.id.menu_item_favorites)
        ratingsMenuItem = menu.findItem(R.id.menu_item_ratings)
        watchlistMenuItem = menu.findItem(R.id.menu_item_watchlists)

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

        setupActionBarWithNavController(navController, appBarConfiguration)

        //TODO: remove to method
        val isSessionIdEmpty = sessionId == ""
        loginButton.isVisible = isSessionIdEmpty
        logoutButton.isVisible = !isSessionIdEmpty
        myListsMenuItem.isVisible = !isSessionIdEmpty
        favoritesMenuItem.isVisible = !isSessionIdEmpty
        ratingsMenuItem.isVisible = !isSessionIdEmpty
        watchlistMenuItem.isVisible = !isSessionIdEmpty

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

                            loginButton.isVisible = false
                            logoutButton.isVisible = true
                            myListsMenuItem.isVisible = true
                            favoritesMenuItem.isVisible = true
                            ratingsMenuItem.isVisible = true
                            watchlistMenuItem.isVisible = true

                            with(encryptedSharedPrefs.edit()) {
                                putString("sessionId", sessionId)
                                putString("avatar", avatar)
                                putLong("accountId", accountId)
                                putString("username", username)
                                putString("includeAdult", includeAdult.toString())
                                putString("name", name)
                                apply()
                            }
                        }

                        navigationView.invalidate()
                        drawerLayout.close()
                    }
                    true
                }
                R.id.logout_button -> {
                    progressBar.visibility = VISIBLE //TODO: to method

                    Api.deleteSession(SessionId(sessionId)).observe(this,
                        { response ->
                            if (response.success == true) {
                                with(encryptedSharedPrefs.edit()) {
                                    clear()
                                    apply()

                                    loginButton.isVisible = true
                                    logoutButton.isVisible = false
                                    myListsMenuItem.isVisible = false
                                    favoritesMenuItem.isVisible = false
                                    ratingsMenuItem.isVisible = false
                                    watchlistMenuItem.isVisible = false

                                    navigationView.invalidate()
                                    progressBar.visibility = GONE
                                    drawerLayout.close()
                                    navController.navigate(R.id.movie_searcher_fragment)
                                }
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
                else -> super.onOptionsItemSelected(menuItem)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_container)

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}