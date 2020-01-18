package com.ishikota.photoviewerandroid

import android.app.Application
import com.ishikota.photoviewerandroid.di.DaggerAppComponent
import com.ishikota.photoviewerandroid.di.NetworkModule
import com.ishikota.photoviewerandroid.infra.flipper.FlipperWrapper
import com.ishikota.photoviewerandroid.infra.timber.ConsoleDebugTree
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

class PhotoViewerApplication: Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(
            NetworkModule(
                endpoint = BuildConfig.API_ENDPOINT,
                appAccessKey = BuildConfig.APP_ACCESS_KEY
            )
        )
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(ConsoleDebugTree())
        }
        FlipperWrapper.initialize(this)
        AndroidThreeTen.init(this)
    }
}
