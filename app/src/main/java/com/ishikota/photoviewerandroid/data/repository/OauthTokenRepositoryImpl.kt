package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import com.ishikota.photoviewerandroid.data.api.PhotoViewerLoginService
import io.reactivex.Completable

class OauthTokenRepositoryImpl(
    private val photoViewerLoginService: PhotoViewerLoginService,
    private val preference: PhotoViewerPreference,
    private val accessKey: String,
    private val secretKey: String,
    private val redirectUri: String
) : OauthTokenRepository {

    override fun isLoggedIn(): Boolean = preference.getBearerToken() != null

    override fun fetchAndSaveBearerToken(code: String): Completable = photoViewerLoginService
        .requestBearerToken(accessKey, secretKey, redirectUri, code, GRANT_TYPE)
        .doOnSuccess { result ->
            preference.saveBearerToken(result.accessToken)
        }.ignoreElement()

    companion object {
        private const val GRANT_TYPE = "authorization_code"
    }
}
