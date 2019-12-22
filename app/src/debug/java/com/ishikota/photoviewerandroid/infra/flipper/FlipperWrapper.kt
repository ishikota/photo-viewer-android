package com.ishikota.photoviewerandroid.infra.flipper

import android.content.Context
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import okhttp3.Interceptor

object FlipperWrapper {
    private val networkFlipperPlugin = NetworkFlipperPlugin()

    fun initialize(context: Context) {
        SoLoader.init(context, false)
        if (FlipperUtils.shouldEnableFlipper(context)) {
            val client = AndroidFlipperClient.getInstance(context)
            client.addPlugin(InspectorFlipperPlugin(context, DescriptorMapping.withDefaults()))
            client.addPlugin(SharedPreferencesFlipperPlugin(context))
            client.addPlugin(DatabasesFlipperPlugin(context))
            client.addPlugin(networkFlipperPlugin)
            client.start()
        }
    }

    fun getOkHttpInterceptor(): Interceptor = FlipperOkhttpInterceptor(networkFlipperPlugin)
}
