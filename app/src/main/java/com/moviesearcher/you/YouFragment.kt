package com.moviesearcher.you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.setFragmentResultListener
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
            buttonLogin.setText(R.string.login_button)
        } else {
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