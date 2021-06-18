package com.moviesearcher

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import com.moviesearcher.api.entity.auth.SessionId
import com.moviesearcher.utils.EncryptedSharedPrefs

private const val TAG = "MovieSearcherActivity"

class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sessionId: String
    private lateinit var authorizeButton: MenuItem
    private lateinit var logoutButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_searcher)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        sessionId = EncryptedSharedPrefs.sharedPrefs(applicationContext)
            .getString("sessionId", "").toString()

        navigationView.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        navigationView.inflateMenu(R.menu.navigation_drawer_menu)
        val menu = navigationView.menu

        authorizeButton = menu.findItem(R.id.login_button)
        logoutButton = menu.findItem(R.id.logout_button)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movie_info_fragment,
                R.id.movie_searcher_fragment,
                R.id.tv_info_fragment,
                R.id.search_result_fragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        authorizeButton.isVisible = sessionId == ""
        logoutButton.isVisible = !authorizeButton.isVisible

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.login_button -> {
                    AuthorizationDialogFragment().show(
                        supportFragmentManager, AuthorizationDialogFragment.TAG
                    )

                    supportFragmentManager.setFragmentResultListener(
                        "sessionId",
                        this
                    ) { _, bundle ->
                        val result = bundle.getString("bundleKey")
                        Log.d(TAG, result.toString())

                        if (result != "") {
                            this.sessionId = result.toString()
                            authorizeButton.isVisible = false
                            logoutButton.isVisible = true
                            navigationView.invalidate()
                            drawerLayout.close()
                        }
                    }
                    true
                }
                R.id.logout_button -> {
                    Api.deleteSession(SessionId(sessionId)).observe(this,
                        { response ->
                            if (response.success == true) {
                                with(EncryptedSharedPrefs.sharedPrefs(baseContext).edit()) {
                                    remove("sessionId").apply()
                                    authorizeButton.isVisible = true
                                    logoutButton.isVisible = false
                                    navigationView.invalidate()
                                    drawerLayout.close()
                                }
                            }
                        })
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