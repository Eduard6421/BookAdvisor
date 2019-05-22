package com.cristidospra.bookadvisor

import android.app.Application
import android.content.Context


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MyApplication.appContext = applicationContext
    }

    companion object {

        var appContext: Context? = null
            private set
    }
}