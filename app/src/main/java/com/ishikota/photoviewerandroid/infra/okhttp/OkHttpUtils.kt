package com.ishikota.photoviewerandroid.infra.okhttp

import com.ishikota.photoviewerandroid.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

fun buildDefaultOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
    if (BuildConfig.DEBUG) {
        addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }
}.build()


