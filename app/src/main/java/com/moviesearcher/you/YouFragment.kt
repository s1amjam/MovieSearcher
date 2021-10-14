package com.moviesearcher.you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.moviesearcher.R
import com.moviesearcher.api.Api
import com.moviesearcher.common.AuthorizationDialogFragment
import com.moviesearcher.common.BaseFragment
import com.moviesearcher.common.model.auth.SessionId
import com.moviesearcher.databinding.FragmentYouBinding

class YouFragment : BaseFragment() {
    private var _binding: FragmentYouBinding? = null
    private val binding get() = _binding!!

    private lateinit var buttonLogin: Button
    private lateinit var watchlistsCardView: CardView
    private lateinit var listsCardView: CardView
    private lateinit var favoritesCardView: CardView
    private lateinit var ratedCardView: CardView

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonLogin = binding.buttonLogin
        watchlistsCardView = binding.watchlistCardView
        listsCardView = binding.listsCardView
        favoritesCardView = binding.favoritesCardView
        ratedCardView = binding.ratedCardView
        navController = findNavController()

        checkIfLoggedIn()

        buttonLogin.setOnClickListener {
            if (sessionId.isNullOrEmpty()) {
                doLogin()
            } else {
                doLogout()
            }
        }
    }

    private fun checkIfLoggedIn() {
        if (sessionId.isNullOrEmpty()) {
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
            watchlistsCardView.setOnClickListener {
                navController.navigate(R.id.fragment_watchlist)
            }
            listsCardView.setOnClickListener {
                navController.navigate(R.id.fragment_my_lists)
            }
            favoritesCardView.setOnClickListener {
                navController.navigate(R.id.fragment_favorites)
            }
            ratedCardView.setOnClickListener {
                navController.navigate(R.id.fragment_rated)
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
        Api.deleteSession(SessionId(sessionId)).observe(viewLifecycleOwner,
            { response ->
                if (response.success == true) {
                    encryptedSharedPrefs.edit().clear().apply()
                    sessionId = ""
                    checkIfLoggedIn()
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}