package com.ishikota.photoviewerandroid.data.api.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchCollections(
    @Json(name = "total") val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "results") val results: List<Collection>
)
