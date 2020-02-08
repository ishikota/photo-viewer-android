package com.ishikota.photoviewerandroid.data

interface PhotoViewerPreference {
    fun saveSearchSuggestionHistories(histories: List<String>)
    fun getSearchSuggestionHistories(): List<String>
}
