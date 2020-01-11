package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id") val id: String,
    @Json(name = "username") val userName: String,
    @Json(name = "name") val name: String,
    @Json(name = "total_collections") val totalCollections: Int,
    @Json(name = "total_likes") val totalLikes: Int,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "profile_image") val profileImage: ProfileImage
) {

    @JsonClass(generateAdapter = true)
    data class ProfileImage(
        @Json(name = "small") val small: String,
        @Json(name = "medium") val medium: String,
        @Json(name = "large") val large: String
    )
}
