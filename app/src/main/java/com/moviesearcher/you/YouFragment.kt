package com.moviesearcher.you

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.cardview.widget.CardView
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moviesearcher.R
import com.moviesearcher.common.AuthorizationDialogFragment
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.common.utils.Constants.DARK_MODE
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.AuthViewModel
import com.moviesearcher.databinding.FragmentYouBinding

class YouFragment : BaseFragment() {
    private var _binding: FragmentYouBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonLogin: Button
    private lateinit var watchlistsCardView: CardView
    private lateinit var listsCardView: CardView
    private lateinit var favoritesCardView: CardView
    private lateinit var ratedCardView: CardView
    private lateinit var titleTv: TextView
    private lateinit var accountLogoIv: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var darkModeIb: ImageButton

    private lateinit var navController: NavController

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isDarkMode = requireContext().getSharedPreferences(DARK_MODE, Context.MODE_PRIVATE)

        navController = findNavController()
        buttonLogin = binding.buttonLogin
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

        checkIfLoggedIn()
        darkModeButtonCheck()

        buttonLogin.setOnClickListener {
            if (sessionId.isEmpty()) {
                doLogin()
            } else {
                doLogout()
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

    private fun checkIfLoggedIn() {
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
            buttonLogin.setText(R.string.login_button)
        } else {
            titleTv.text = encryptedSharedPrefs.getString("username", "")
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
            buttonLogin.setText(R.string.logout_button)
        }
    }

    private fun doLogin() {
        AuthorizationDialogFragment().show(parentFragmentManager, AuthorizationDialogFragment.TAG)
        setFragmentResultListener("accountResponse") { _, bundle ->
            val sessionId = bundle.getString("sessionId")
            val avatar = bundle.getString("avatar")
            val accountId = bundle.getLong("accountId")
            val username = bundle.getString("username")
            val includeAdult = bundle.getString("includeAdult")
            val name = bundle.getString("name")

            if (!sessionId.isNullOrEmpty()) {
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
                checkIfLoggedIn()
            }
        }
    }

    private fun doLogout() {
        viewModel.getDeleteSession(SessionId(sessionId)).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let {
                        progressBar.visibility = View.GONE

                        Toast.makeText(
                            requireContext(),
                            "You was successfully logged out",
                            Toast.LENGTH_SHORT
                        ).show()

                        encryptedSharedPrefs.edit().clear().apply()
                        sessionId = ""
                        checkIfLoggedIn()
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