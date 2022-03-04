package com.moviesearcher.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.moviesearcher.common.model.account.Avatar
import com.moviesearcher.common.model.auth.RequestToken
import com.moviesearcher.common.utils.Constants
import com.moviesearcher.common.utils.Constants.SUCCESS_SESSION_URL
import com.moviesearcher.common.utils.Status
import com.moviesearcher.common.viewmodel.AuthViewModel
import com.moviesearcher.databinding.FragmentAuthorizationDialogBinding
import com.moviesearcher.watchlist.common.viewmodel.ERROR_MESSAGE

class AuthorizationDialogFragment : DialogFragment() {
    private var _binding: FragmentAuthorizationDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var webView: WebView
    private lateinit var requestToken: String
    private lateinit var progressBar: ProgressBar

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationDialogBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBarAuthorizationDialog
        webView = binding.webViewAuthorizationDialog
        webView.settings.javaScriptEnabled = true

        viewModel.getToken().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { response ->
                        webView.loadUrl(Constants.AUTH_URL.format(response.requestToken))
                        requestToken = response.requestToken.toString()
                        val constraintLayout = binding.fragmentAuthorizationDialogConstraintLayout
                        dialog?.window?.setContentView(constraintLayout)
                        progressBar.visibility = GONE
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = GONE
                }
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                val dialogWindow = dialog?.window
                dialogWindow?.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                )
                dialogWindow
                    ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)

                if (url.equals(SUCCESS_SESSION_URL.format(requestToken))) {
                    viewModel.getSession(RequestToken(requestToken)).observe(viewLifecycleOwner) {
                        when (it.status) {
                            Status.SUCCESS -> {
                                it.data?.let { response ->
                                    if (!response.sessionId.isNullOrEmpty()) {
                                        observeAccount(response.sessionId)
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "Something went wrong. Try again later.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        progressBar.visibility = GONE
                                        dismiss()
                                    }
                                }
                            }
                            Status.LOADING -> {
                                progressBar.visibility = GONE
                            }
                            Status.ERROR -> {
                                Toast.makeText(
                                    requireContext(),
                                    ERROR_MESSAGE.format(it.message),
                                    Toast.LENGTH_LONG
                                ).show()
                                progressBar.visibility = GONE
                            }
                        }
                    }
                }
            }
        }
    }

    fun observeAccount(sessionId: String) {
        var username: String?
        var accountId: Long
        var name: String?
        var includeAdult: Boolean?
        var avatar: Avatar?

        viewModel.getAccount(sessionId).observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { accountResponse ->
                        username = accountResponse.username
                        accountId = accountResponse.id!!.toLong()
                        name = accountResponse.name
                        includeAdult = accountResponse.includeAdult!!
                        avatar = accountResponse.avatar

                        parentFragmentManager.setFragmentResult(
                            "accountResponse",
                            bundleOf(
                                "sessionId" to sessionId,
                                "includeAdult" to includeAdult.toString(),
                                "accountId" to accountId,
                                "avatar" to avatar?.gravatar?.hash,
                                "username" to username,
                                "name" to name,
                            )
                        )

                        progressBar.visibility = GONE
                        dismiss()
                    }
                }
                Status.LOADING -> {
                    progressBar.visibility = VISIBLE
                }
                Status.ERROR -> {
                    Toast.makeText(
                        requireContext(),
                        ERROR_MESSAGE.format(it.message),
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AuthorizationDialogFragment"
    }
}