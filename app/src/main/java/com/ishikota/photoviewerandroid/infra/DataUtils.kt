package com.ishikota.photoviewerandroid.infra

import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

private val APP_DATE_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME

fun String.toOffsetDateTime(pattern: String? = null): OffsetDateTime {
    val formatter = if (pattern == null) APP_DATE_FORMATTER else DateTimeFormatter.ofPattern(pattern)
    return OffsetDateTime.parse(this, formatter)
}

fun OffsetDateTime.format(pattern: String? = null): String {
    val formatter = if (pattern == null) APP_DATE_FORMATTER else DateTimeFormatter.ofPattern(pattern)
    return format(formatter)
}
