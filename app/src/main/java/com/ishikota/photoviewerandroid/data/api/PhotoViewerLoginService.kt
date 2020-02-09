package com.ishikota.photoviewerandroid.data.api

import com.ishikota.photoviewerandroid.data.api.entities.BearerTokenRequestResult
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PhotoViewerLoginService {

    @FormUrlEncoded
    @POST("/oauth/token")
    fun requestBearerToken(
        @Field("client_id") accessKey: String,
        @Field("client_secret") secretKey: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String,
        @Field("grant_type") grantType: String
    ): Single<BearerTokenRequestResult>

}
