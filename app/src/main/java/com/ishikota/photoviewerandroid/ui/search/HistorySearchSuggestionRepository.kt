package com.ishikota.photoviewerandroid.ui.search

import android.annotation.SuppressLint
import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import io.reactivex.Single

// Save search history up to 10 words.
// Old history is deleted by FIFO.
class HistorySearchSuggestionRepository(
    private val preference: PhotoViewerPreference,
    private val capacity: Int = HISTORY_CAPACITY
) {

    fun rememberQuery(query: String) {
        val histories = preference.getSearchSuggestionHistories().toMutableList()

        // do nothing if query is already in history
        if (histories.contains(query)) {
            return
        }

        // drop oldest entry if history is full of entries
        if (histories.size >= capacity) {
            histories.removeAt(0)
        }

        histories.add(query)
        preference.saveSearchSuggestionHistories(histories)
    }

    @SuppressLint("DefaultLocale")
    fun getSuggestions(query: String): Single<List<SearchSuggestionRecyclerViewAdapter.Item>> =
        Single.just(
            preference.getSearchSuggestionHistories()
                .filter { it.toLowerCase().contains(query.toLowerCase()) }
                .map { history ->
                    SearchSuggestionRecyclerViewAdapter.Item(history)
                }
        )

    companion object {
        private const val HISTORY_CAPACITY = 10
    }
}
