package com.duke.diskoverusosyal.application

import android.app.Application
import com.duke.diskoverusosyal.utils.Utils

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(applicationContext)
    }
}