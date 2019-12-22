package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.OffsetDateTime

@JsonClass(generateAdapter = true)
data class Photo(
    @Json(name = "id") val id: String,
    @Json(name = "created_at") val createdAt: OffsetDateTime,
    @Json(name = "updated_at") val updatedAt: OffsetDateTime,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "color") val color: String,
    @Json(name = "likes") val likes: Int,
    @Json(name = "description") val description: String?,
    @Json(name = "alt_description") val altDescription: String,
    @Json(name = "urls") val urls: Urls,
    @Json(name = "user") val user: User
) {

    @JsonClass(generateAdapter = true)
    data class Urls(
        @Json(name = "raw") val raw: String,
        @Json(name = "full") val full: String,
        @Json(name = "regular") val regular: String,
        @Json(name = "small") val small: String,
        @Json(name = "thumb") val thumb: String
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "id") val id: String,
        @Json(name = "username") val userName: String,
        @Json(name = "name") val name: String,
        @Json(name = "total_collections") val totalCollections: Int,
        @Json(name = "total_likes") val totalLikes: Int,
        @Json(name = "total_photos") val totalPhotos: Int
    ) {

        @JsonClass(generateAdapter = true)
        data class ProfileImage(
            @Json(name = "small") val small: String,
            @Json(name = "medium") val medium: String,
            @Json(name = "large") val large: String
        )
    }
}
