package com.alexnemyr

import android.app.Application
import com.alexnemyr.testtaska.BuildConfig
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}