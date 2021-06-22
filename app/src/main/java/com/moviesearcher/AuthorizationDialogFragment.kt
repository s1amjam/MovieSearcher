package com.moviesearcher

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.moviesearcher.api.Api
import com.moviesearcher.api.entity.auth.RequestToken
import com.moviesearcher.utils.Constants
import com.moviesearcher.utils.Constants.SUCCESS_SESSION_URL

class AuthorizationDialogFragment : DialogFragment() {
    private lateinit var webView: WebView
    private lateinit var requestToken: String

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        webView = WebView(requireActivity().applicationContext)
        webView.settings.javaScriptEnabled = true

        Api.createRequestToken().observe(requireActivity(),
            { response ->
                webView.loadUrl(Constants.AUTH_URL.format(response.requestToken))
                requestToken = response.requestToken.toString()
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
                lateinit var sessionId: String

                if (url.equals(SUCCESS_SESSION_URL.format(requestToken))) {
                    Api.createSession(RequestToken(requestToken))
                        .observe(requireActivity(), { response ->
                            sessionId = response.sessionId.toString()

                            parentFragmentManager.setFragmentResult(
                                "sessionIdKey",
                                bundleOf("sessionId" to sessionId)
                            )

                            dismiss()
                        })
                }
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(webView)
            .create()
    }

    companion object {
        const val TAG = "AuthorizationDialogFragment"
    }
}