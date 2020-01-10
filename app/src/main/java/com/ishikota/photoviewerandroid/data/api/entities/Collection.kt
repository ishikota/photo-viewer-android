package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.OffsetDateTime

@JsonClass(generateAdapter = true)
data class Collection(
    @Json(name = "id") val id: String,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "published_at") val publishedAt: OffsetDateTime,
    @Json(name = "updated_at") val updatedAt: OffsetDateTime,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "cover_photo") val coverPhoto: Photo,
    @Json(name = "user") val user: User
)
