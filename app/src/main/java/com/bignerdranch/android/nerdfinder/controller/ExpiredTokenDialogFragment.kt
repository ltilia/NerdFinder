package com.bignerdranch.android.nerdfinder.controller

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bignerdranch.android.nerdfinder.R

class ExpiredTokenDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setTitle(R.string.expired_token_dialog_title)
                .setMessage(R.string.expired_token_dialog_message)
                .setPositiveButton(android.R.string.ok
                ) { dialog, i ->
                    val intent = Intent(context,
                            AuthenticationActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
    }
}
