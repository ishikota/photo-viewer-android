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
    @Json(name = "alt_description") val altDescription: String?,
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
}
