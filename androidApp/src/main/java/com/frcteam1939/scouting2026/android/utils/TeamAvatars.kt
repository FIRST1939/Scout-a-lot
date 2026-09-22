package com.frcteam1939.scouting2026.android.utils

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.applicationContext
import com.frcteam1939.scouting2026.DataDependencyContainer.frcEvents
import java.io.File
import java.io.IOException

suspend fun loadTeamAvatarsByEvent(eventKey: String) {
    frcEvents.pullTeamAvatarsByEvent(eventKey).forEach {
        val team = it.first
        val avatarData = it.second
        val fileName = "frc${team}avatar"

        val file = File(applicationContext.filesDir, fileName)

        if (file.exists()) {
            println("icon for team $team already exists")
        } else {
            try {
                applicationContext.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                    outputStream.write(avatarData)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

fun getTeamAvatar(team: String): ImageBitmap {
    return try {
        val file = File(applicationContext.filesDir, "frc${team}avatar")

        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            ?: BitmapFactory.decodeResource(applicationContext.resources, R.drawable.noicon)

        bitmap.asImageBitmap()
    } catch (e: IOException) {
        e.printStackTrace()
        BitmapFactory.decodeResource(applicationContext.resources, R.drawable.noicon).asImageBitmap()
    }
}