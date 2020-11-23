package com.bignerdranch.android.nerdfinder.controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this)
        setContentView(webView)
    }

    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, AuthenticationActivity::class.java)
        }
    }
}
