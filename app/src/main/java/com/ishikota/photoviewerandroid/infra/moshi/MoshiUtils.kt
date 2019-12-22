package com.ishikota.photoviewerandroid.infra.moshi

import com.ishikota.photoviewerandroid.infra.format
import com.ishikota.photoviewerandroid.infra.toOffsetDateTime
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import org.threeten.bp.OffsetDateTime

fun buildDefaultMoshi() = Moshi.Builder()
    .add(DateAdapter())
    .build()

private class DateAdapter {
    @ToJson
    fun toJson(date: OffsetDateTime): String = date.format()

    @FromJson
    fun fromJson(json: String): OffsetDateTime = json.toOffsetDateTime()
}
