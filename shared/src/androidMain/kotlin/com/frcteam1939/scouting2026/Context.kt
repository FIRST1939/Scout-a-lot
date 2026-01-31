package com.frcteam1939.scouting2026

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

lateinit var applicationContext: Context
private var currentActivity: WeakReference<Activity>? = null

val activeActivity: Activity?
    get() = currentActivity?.get()

fun initAppContext(context: Context) {
    applicationContext = context.applicationContext
}

fun setActivity(activity: Activity) {
    currentActivity = WeakReference(activity)
}