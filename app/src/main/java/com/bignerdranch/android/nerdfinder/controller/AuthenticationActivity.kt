package com.bignerdranch.android.nerdfinder.controller

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.nerdfinder.helper.FoursquareOauthUriHelper
import com.bignerdranch.android.nerdfinder.model.TokenStore
import com.bignerdranch.android.nerdfinder.web.DataManager

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var dataManager:DataManager
    private val webViewClient = object :WebViewClient(){
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            val url = request?.url.toString()
            if(url.contains(DataManager.OAUTH_REDIRECT_URI)) {
                FoursquareOauthUriHelper.getAccessToken(url)?.let { token->
                    val tokenStore = TokenStore.getInstance(this@AuthenticationActivity)
                    tokenStore.accessToken = token
                }
                finish()
            }
            return false
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = webViewClient
        dataManager = DataManager.get()
        webView.loadUrl(dataManager.getAuthenticationUrl())
        setContentView(webView)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
        }
    }
}
