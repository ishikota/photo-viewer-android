package com.ishikota.photoviewerandroid.data

import android.content.SharedPreferences
import androidx.core.content.edit

class PhotoViewerPreferenceImpl(
    private val preference: SharedPreferences
) : PhotoViewerPreference {
    override fun saveSearchSuggestionHistories(histories: List<String>) {
        val str = histories.joinToString(",")
        preference.edit { putString(KEY_SEARCH_SUGGESTION_HISTORIES, str) }
    }

    override fun getSearchSuggestionHistories(): List<String> {
        val str = preference.getString(KEY_SEARCH_SUGGESTION_HISTORIES, null)
        return str?.split(",") ?: emptyList()
    }

    companion object {
        private const val KEY_SEARCH_SUGGESTION_HISTORIES = "KEY_SEARCH_SUGGESTION_HISTORIES"
    }
}
