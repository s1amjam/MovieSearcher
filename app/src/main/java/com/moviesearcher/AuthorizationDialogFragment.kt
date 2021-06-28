package com.moviesearcher

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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.auth.RequestToken
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.SUCCESS_SESSION_URL

class AuthorizationDialogFragment : DialogFragment() {
    private lateinit var webView: WebView
    private lateinit var requestToken: String
    private lateinit var progressBar: ProgressBar
    private lateinit var username: String
    private lateinit var name: String
    private lateinit var avatar: String

    private var id: Int? = null
    private var includeAdult = false


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.fragment_authorization_dialog)
            .create()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_authorization_dialog, container, false)

        progressBar = view.findViewById(R.id.progress_bar_authorization_dialog)
        webView = view.findViewById(R.id.web_view_authorization_dialog)
        webView.settings.javaScriptEnabled = true

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Api.createRequestToken().observe(requireActivity(),
            { response ->
                webView.loadUrl(Constants.AUTH_URL.format(response.requestToken))
                requestToken = response.requestToken.toString()
                val cl =
                    view.findViewById<ConstraintLayout>(R.id.fragment_authorization_dialog_constraint_layout)
                dialog?.window?.setContentView(cl)
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
                val sessionId: MutableLiveData<String> = MutableLiveData()

                if (url.equals(SUCCESS_SESSION_URL.format(requestToken))) {
                    progressBar.visibility = VISIBLE
                    val parentFragmentManager = parentFragmentManager

                    Api.createSession(RequestToken(requestToken))
                        .observe(requireActivity(), { response ->
                            sessionId.value = response.sessionId

                            Api.getAccount(sessionId.value.toString())
                                .observe(requireActivity(), { accountResponse ->
                                    username = accountResponse.username.toString()
                                    id = accountResponse.id
                                    name = accountResponse.name.toString()
                                    includeAdult = accountResponse.includeAdult!!
                                    avatar = accountResponse.avatar.toString()

                                    parentFragmentManager.setFragmentResult(
                                        "accountResponse",
                                        bundleOf(
                                            "sessionId" to sessionId.value,
                                            "includeAdult" to includeAdult.toString(),
                                            "id" to id.toString(),
                                            "avatar" to avatar,
                                            "username" to username,
                                            "name" to name,
                                        )
                                    )
                                })

                            progressBar.visibility = View.GONE
                            dismiss()
                        })
                }
            }
        }
    }

    companion object {
        const val TAG = "AuthorizationDialogFragment"
    }
}