package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BearerTokenRequestResult(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "scope") val scope: String,
    @Json(name = "created_at") val createdAt: Int
)
