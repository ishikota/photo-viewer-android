package com.ishikota.photoviewerandroid.data.api.entities

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id") val id: String,
    @Json(name = "username") val userName: String,
    @Json(name = "name") val name: String,
    @Json(name = "first_name") val firstName: String?,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "bio") val bio: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "total_collections") val totalCollections: Int,
    @Json(name = "total_likes") val totalLikes: Int,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "followers_count") val followersCount: Int?,
    @Json(name = "following_count") val followingCount: Int?,
    @Json(name = "downloads") val downloads: Int?,
    @Json(name = "profile_image") val profileImage: ProfileImage
): Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class ProfileImage(
        @Json(name = "small") val small: String,
        @Json(name = "medium") val medium: String,
        @Json(name = "large") val large: String
    ): Parcelable
}
