package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoLikeResult(
    @Json(name = "photo") val photo: Photo,
    @Json(name = "user") val user: User
)
