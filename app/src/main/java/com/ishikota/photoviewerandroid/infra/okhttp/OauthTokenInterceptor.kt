package com.ishikota.photoviewerandroid.infra.okhttp

import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import okhttp3.Interceptor
import okhttp3.Response

class OauthTokenInterceptor(private val preference: PhotoViewerPreference) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = preference.getBearerToken()
        return if (token ==  null) {
            chain.proceed(chain.request())
        } else {
            val updatedRequest = chain.request().newBuilder()
                .removeHeader(HEADER_NAME)
                .addHeader(HEADER_NAME, "Bearer $token")
                .build()
            chain.proceed(updatedRequest)
        }
    }

    companion object {
        private const val HEADER_NAME = "Authorization"
    }
}
