package com.ishikota.photoviewerandroid

import android.app.Application
import com.ishikota.photoviewerandroid.infra.timber.ConsoleDebugTree
import timber.log.Timber

class PhotoViewerApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(ConsoleDebugTree())
        }
    }
}