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
import com.moviesearcher.api.entity.auth.SessionId
import com.moviesearcher.utils.EncryptedSharedPrefs

private const val TAG = "MovieSearcherActivity"
//TODO: remove everything to a separate class?
class MovieSearcherActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sessionId: String
    private lateinit var loginButton: MenuItem
    private lateinit var logoutButton: MenuItem
    private lateinit var myListsButton: MenuItem
    private lateinit var encryptedSharedPrefs: SharedPreferences
    private lateinit var progressBar: ProgressBar

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
        sessionId = encryptedSharedPrefs
            .getString("sessionId", "").toString()

        navigationView.setupWithNavController(navController)
        setSupportActionBar(toolbar)
        navigationView.inflateMenu(R.menu.navigation_drawer_menu)
        val menu = navigationView.menu

        loginButton = menu.findItem(R.id.login_button)
        logoutButton = menu.findItem(R.id.logout_button)
        myListsButton = menu.findItem(R.id.my_lists_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movie_info_fragment,
                R.id.movie_searcher_fragment,
                R.id.tv_info_fragment,
                R.id.search_result_fragment,
                R.id.my_lists_fragment,
                R.id.my_list_fragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)

        val isSessionIdEmpty = sessionId == ""
        loginButton.isVisible = isSessionIdEmpty
        logoutButton.isVisible = !isSessionIdEmpty
        myListsButton.isVisible = !isSessionIdEmpty

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
                        var avatar = bundle.getString("avatar")
                        var id = bundle.getString("id")
                        var username = bundle.getString("username")
                        var includeAdult = bundle.getString("includeAdult")
                        var name = bundle.getString("name")

                        if (sessionId != null || sessionId != "") {
                            this.sessionId = sessionId.toString()
                            avatar = avatar.toString()
                            id = id.toString()
                            username = username.toString()
                            includeAdult = includeAdult.toString()
                            name = name.toString()

                            loginButton.isVisible = false
                            logoutButton.isVisible = true
                            myListsButton.isVisible = true
                        }

                        with(
                            encryptedSharedPrefs.edit()
                        ) {
                            putString("sessionId", sessionId)
                            putString("avatar", avatar)
                            putString("id", id.toString())
                            putString("username", username)
                            putString("includeAdult", includeAdult.toString())
                            putString("name", name)
                            apply()
                        }

                        navigationView.invalidate()
                        drawerLayout.close()
                    }
                    true
                }
                R.id.logout_button -> {
                    progressBar.visibility = VISIBLE

                    Api.deleteSession(SessionId(sessionId)).observe(this,
                        { response ->
                            if (response.success == true) {
                                with(encryptedSharedPrefs.edit()) {
                                    remove("sessionId")
                                    remove("avatar")
                                    remove("id")
                                    remove("username")
                                    remove("includeAdult")
                                    remove("name")
                                        .apply()

                                    loginButton.isVisible = true
                                    logoutButton.isVisible = false
                                    myListsButton.isVisible = false

                                    navigationView.invalidate()
                                    progressBar.visibility = GONE
                                    drawerLayout.close()
                                }
                            }
                        })
                    true
                }
                R.id.my_lists_fragment -> {
                    navController.navigate(R.id.my_lists_fragment)

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