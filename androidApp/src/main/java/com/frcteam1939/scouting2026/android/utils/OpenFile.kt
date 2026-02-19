package com.frcteam1939.scouting2026.android.utils

import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import androidx.compose.runtime.Composable

@Composable
fun openFile(pickerInitialUri: Uri) {
    val intent =
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
    // startActivityForResult(intent, PICK_PDF_FILE)
}
