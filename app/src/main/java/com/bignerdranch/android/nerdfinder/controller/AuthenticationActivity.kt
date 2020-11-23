package com.bignerdranch.android.nerdfinder.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val webViewClient = object :WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return false
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = webViewClient
        setContentView(webView)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
        }
    }
}
