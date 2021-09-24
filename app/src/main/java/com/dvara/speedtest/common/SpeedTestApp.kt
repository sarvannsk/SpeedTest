package com.dvara.speedtest.common

import android.app.Application
import com.dvara.speedtest.common.AppPreferences

class SpeedTestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.init(this)
    }
}