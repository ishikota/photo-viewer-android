package com.ishikota.photoviewerandroid.data.repository

import io.reactivex.Completable

interface OauthTokenRepository {
    fun isLoggedIn(): Boolean
    fun fetchAndSaveBearerToken(code: String): Completable
    fun logout()
}
