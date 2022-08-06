package com.moviesearcher.you

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moviesearcher.R
import com.moviesearcher.common.AuthorizationDialogFragment
import com.moviesearcher.common.credentials.CredentialsHolder
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.common.utils.Constants.DARK_MODE
import com.moviesearcher.common.utils.Constants.ERROR_MESSAGE
import com.moviesearcher.common.utils.Constants.USER_DATA
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.AuthViewModel
import com.moviesearcher.databinding.FragmentYouBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YouFragment : Fragment() {

    private var _binding: FragmentYouBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var credentialsHolder: CredentialsHolder
    private val sessionId: String
        get() = credentialsHolder.getSessionId()

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var watchlistsCardView: CardView
    private lateinit var listsCardView: CardView
    private lateinit var favoritesCardView: CardView
    private lateinit var ratedCardView: CardView
    private lateinit var titleTv: TextView
    private lateinit var accountLogoIv: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var darkModeIb: ImageButton
    private lateinit var loginIb: ImageButton

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isDarkMode = requireContext().getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE)
        val userData = requireContext().getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)

        navController = findNavController()
        loginIb = binding.loginIb
        watchlistsCardView = binding.watchlistCardView
        listsCardView = binding.listsCardView
        favoritesCardView = binding.favoritesCardView
        ratedCardView = binding.ratedCardView
        titleTv = binding.titleTv
        accountLogoIv = binding.accountLogoIv
        progressBar = binding.progressBar
        darkModeIb = binding.darkModeIb

        fun getIsDarkMode(): Boolean {
            return isDarkMode.getInt(DARK_MODE, 1) == MODE_NIGHT_YES
        }

        fun darkModeButtonCheck() {
            if (getIsDarkMode()) {
                darkModeIb.setImageResource(R.drawable.ic_baseline_light_mode_36)
            }
        }

        checkIfLoggedIn(userData)
        darkModeButtonCheck()

        loginIb.setOnClickListener {
            if (sessionId.isEmpty()) {
                doLogin(userData)
            } else {
                doLogout(userData)
            }
        }

        darkModeIb.setOnClickListener {
            if (getIsDarkMode()) {
                isDarkMode.edit().putInt(DARK_MODE, MODE_NIGHT_NO).apply()
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                darkModeIb.setImageResource(R.drawable.ic_baseline_light_mode_36)
            } else {
                isDarkMode.edit().putInt(DARK_MODE, MODE_NIGHT_YES).apply()
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                darkModeIb.setImageResource(R.drawable.ic_baseline_dark_mode_24)
            }
        }
    }

    private fun checkIfLoggedIn(userData: SharedPreferences) {
        if (sessionId.isEmpty()) {
            titleTv.setText(R.string.please_log_in)
            accountLogoIv.visibility = View.GONE

            watchlistsCardView.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Please log in to see your watchlist",
                    Toast.LENGTH_SHORT
                ).show()
            }
            listsCardView.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Please log in to see your lists",
                    Toast.LENGTH_SHORT
                ).show()
            }
            favoritesCardView.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Please log in to see your favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ratedCardView.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Please log in to see your rated movies",
                    Toast.LENGTH_SHORT
                ).show()
            }
            loginIb.setImageResource(R.drawable.ic_round_login_36)
        } else {
            titleTv.text = userData.getString("username", "")
            accountLogoIv.visibility = View.VISIBLE

            watchlistsCardView.setOnClickListener {
                navController.navigate(YouFragmentDirections.actionYouFragmentToFragmentWatchlist())
            }
            listsCardView.setOnClickListener {
                navController.navigate(YouFragmentDirections.actionYouFragmentToFragmentMyLists())
            }
            favoritesCardView.setOnClickListener {
                navController.navigate(YouFragmentDirections.actionYouFragmentToFragmentFavorites())
            }
            ratedCardView.setOnClickListener {
                navController.navigate(YouFragmentDirections.actionYouFragmentToFragmentRated())
            }
            loginIb.setImageResource(R.drawable.ic_round_logout_24)
        }
    }

    private fun doLogin(userData: SharedPreferences) {
        AuthorizationDialogFragment().show(parentFragmentManager, AuthorizationDialogFragment.TAG)
        setFragmentResultListener("accountResponse") { _, bundle ->
            val sessionId = bundle.getString("sessionId")
            val avatar = bundle.getString("avatar")
            val accountId = bundle.getLong("accountId")
            val username = bundle.getString("username")
            val includeAdult = bundle.getString("includeAdult")
            val name = bundle.getString("name")

            if (!sessionId.isNullOrEmpty()) {
                credentialsHolder.putSessionId(sessionId.toString())
                credentialsHolder.putAccountId(accountId)

                with(userData.edit()) {
                    putString("avatar", avatar)
                    putString("username", username)
                    putString("includeAdult", includeAdult.toString())
                    putString("name", name)
                    apply()
                }
                checkIfLoggedIn(userData)
            }
        }
    }

    private fun doLogout(userData: SharedPreferences) {
        viewModel.getDeleteSession(SessionId(sessionId)).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        progressBar.visibility = View.GONE

                        Toast.makeText(
                            requireContext(),
                            "You've been successfully logged out",
                            Toast.LENGTH_SHORT
                        ).show()

                        userData.edit().clear().apply()
                        credentialsHolder.putSessionId("")
                        checkIfLoggedIn(userData)
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}