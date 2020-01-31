package com.ishikota.photoviewerandroid.ui.search

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.content.edit
import io.reactivex.Single

// Save search history up to 10 words.
// Old history is deleted by FIFO.
class HistorySearchSuggestionRepository(
    context: Context,
    private val capacity: Int = HISTORY_CAPACITY
) {

    private val preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun rememberQuery(query: String) {
        val histories = getHistories().toMutableList()

        // do nothing if query is already in history
        if (histories.contains(query)) {
            return
        }

        // drop oldest entry if history is full of entries
        if (histories.size >= capacity) {
            histories.removeAt(0)
        }

        histories.add(query)
        saveHistories(histories)
    }

    @SuppressLint("DefaultLocale")
    fun getSuggestions(query: String): Single<List<SearchSuggestionRecyclerViewAdapter.Item>> =
        Single.just(
            getHistories()
                .filter { it.toLowerCase().contains(query.toLowerCase()) }
                .map { history ->
                    SearchSuggestionRecyclerViewAdapter.Item(history)
                }
        )

    private fun getHistories(): List<String> {
        val str = preference.getString(KEY_HISTORY, null)
        return str?.split(",") ?: emptyList()
    }

    private fun saveHistories(history: List<String>) {
        val str = history.joinToString(",")
        preference.edit { putString(KEY_HISTORY, str) }
    }

    companion object {
        private const val HISTORY_CAPACITY = 10
        private const val PREF_NAME = "search_suggestion_history_preference"
        private const val KEY_HISTORY = "KEY_HISTORY"
    }
}
