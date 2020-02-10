package com.ishikota.photoviewerandroid.data

interface PhotoViewerPreference {
    fun saveSearchSuggestionHistories(histories: List<String>)
    fun getSearchSuggestionHistories(): List<String>
    fun saveBearerToken(token: String)
    fun getBearerToken(): String?
}
