package com.moviesearcher.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.moviesearcher.databinding.FragmentVideoBinding
import com.moviesearcher.utils.Constants

class VideoFragment : Fragment() {
    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieKey = arguments?.get("video_key")

        webView = binding.videoWebview
        webView.webViewClient = object : WebViewClient() {}
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(Constants.YOUTUBE_VIDEO_URL.format(movieKey))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}