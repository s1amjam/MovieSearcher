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
    private lateinit var loginButton: MenuItem
    private lateinit var logoutButton: MenuItem
    private lateinit var myListsButton: MenuItem

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

        loginButton = menu.findItem(R.id.login_button)
        logoutButton = menu.findItem(R.id.logout_button)
        myListsButton = menu.findItem(R.id.my_lists_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movie_info_fragment,
                R.id.movie_searcher_fragment,
                R.id.tv_info_fragment,
                R.id.search_result_fragment,
                R.id.my_lists_fragment
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
                        "sessionIdKey",
                        this
                    ) { _, bundle ->
                        val result = bundle.getString("sessionId")
                        var includeAdult: Boolean
                        var id: Int?
                        lateinit var avatar: String
                        lateinit var username: String
                        lateinit var name: String

                        if (result != null || result != "") {
                            this.sessionId = result.toString()
                            loginButton.isVisible = false
                            logoutButton.isVisible = true
                            myListsButton.isVisible = true

                            navigationView.invalidate()
                            drawerLayout.close()

                            Api.getAccount(sessionId)
                                .observe(this, { accountResponse ->
                                    username = accountResponse.username.toString()
                                    id = accountResponse.id
                                    name = accountResponse.name.toString()
                                    includeAdult = accountResponse.includeAdult!!
                                    avatar = accountResponse.avatar.toString()
                                    Log.d(TAG, username)
                                    with(
                                        EncryptedSharedPrefs.sharedPrefs(applicationContext).edit()
                                    ) {
                                        putString("sessionId", sessionId)
                                        putString("avatar", avatar)
                                        putString("id", id.toString())
                                        putString("username", username)
                                        putString("includeAdult", includeAdult.toString())
                                        putString("name", name)
                                        apply()
                                    }
                                })
                        }
                    }
                    true
                }
                R.id.logout_button -> {
                    Api.deleteSession(SessionId(sessionId)).observe(this,
                        { response ->
                            if (response.success == true) {
                                with(EncryptedSharedPrefs.sharedPrefs(applicationContext).edit()) {
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
                                    drawerLayout.close()
                                }
                            }
                        })
                    true
                }
                R.id.my_lists_fragment -> {
                    val sessionId = EncryptedSharedPrefs.sharedPrefs(applicationContext)
                        .getString("sessionId", null)
                    val id: Int? =
                        EncryptedSharedPrefs.sharedPrefs(applicationContext).getString("id", null)
                            ?.toInt()
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