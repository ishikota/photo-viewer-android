package com.ishikota.photoviewerandroid.infra.flipper

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

object FlipperWrapper {

    fun initialize(context: Context) {
        // do nothing
    }

    // just bypass request
    fun getOkHttpInterceptor() = object: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
    }
}
