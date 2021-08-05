package com.moviesearcher.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.moviesearcher.api.Api
import com.moviesearcher.common.model.account.Avatar
import com.moviesearcher.common.model.auth.RequestToken
import com.moviesearcher.databinding.FragmentAuthorizationDialogBinding
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.SUCCESS_SESSION_URL

class AuthorizationDialogFragment : DialogFragment() {
    private var _binding: FragmentAuthorizationDialogBinding? = null
    private val binding get() = _binding!!

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
        val view = binding.root

        progressBar = binding.progressBarAuthorizationDialog
        webView = binding.webViewAuthorizationDialog
        webView.settings.javaScriptEnabled = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Api.createRequestToken().observe(requireActivity(),
            { response ->
                webView.loadUrl(Constants.AUTH_URL.format(response.requestToken))
                requestToken = response.requestToken.toString()
                val constraintLayout = binding.fragmentAuthorizationDialogConstraintLayout
                dialog?.window?.setContentView(constraintLayout)
            })

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
                var sessionId: String?
                var username: String?
                var accountId: Long
                var name: String?
                var includeAdult: Boolean?
                var avatar: Avatar?

                if (url.equals(SUCCESS_SESSION_URL.format(requestToken))) {
                    progressBar.visibility = VISIBLE
                    val parentFragmentManager = parentFragmentManager

                    Api.createSession(RequestToken(requestToken))
                        .observe(requireActivity(), { response ->
                            sessionId = response.sessionId

                            Api.getAccount(sessionId)
                                .observe(requireActivity(), { accountResponse ->
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

                                    progressBar.visibility = View.GONE
                                    dismiss()
                                })
                        })
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