package com.frcteam1939.scouting2026

import android.app.AlertDialog
import android.os.Handler
import android.os.Looper

actual fun showAlert (title: String, message: String) {
    Handler(Looper.getMainLooper()).post {
        val context = activeActivity
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
    