package com.ishikota.photoviewerandroid.infra.okhttp

import okhttp3.Interceptor
import okhttp3.Response

class ApiAccessKeyInterceptor(private val accessKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Client-ID $accessKey")
            .build()
        return chain.proceed(request)
    }
}
