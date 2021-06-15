package com.moviesearcher

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.moviesearcher.api.Api
import com.moviesearcher.utils.Constants

class AuthorizationDialogFragment : DialogFragment() {
    private lateinit var webView: WebView
    lateinit var sharedPref: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        sharedPref = activity?.getSharedPreferences("AppPrefs", MODE_PRIVATE)!!

        webView = WebView(requireActivity().applicationContext)
        webView.settings.javaScriptEnabled = true

        val requestToken = Api.createRequestToken().observe(requireActivity(),
            { response ->
                webView.loadUrl(Constants.AUTH_URL.format(response.requestToken))
            })

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

                //val session = Api.createSession(RequestToken(requestToken?.requestToken!!))
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