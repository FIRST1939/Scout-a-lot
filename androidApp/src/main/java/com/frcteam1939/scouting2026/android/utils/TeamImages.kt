package com.frcteam1939.scouting2026.android.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.frcteam1939.scouting2026.android.R
import com.frcteam1939.scouting2026.applicationContext
import java.io.IOException

fun getTeamImage(team: String): Drawable? {
    val imageName = "frc${team}robot.jpg"
    return try {
        val inputStream = applicationContext.assets.open(imageName)
        val drawable = Drawable.createFromStream(inputStream, null)
        inputStream.close()
        drawable
    } catch (e: IOException) {
        val resId = R.drawable.missing_image
        val drawable = ContextCompat.getDrawable(applicationContext, resId)
        drawable
    }
}