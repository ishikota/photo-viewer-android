package com.ishikota.photoviewerandroid.ui.search

import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val repository: HistorySearchSuggestionRepository
) : ViewModel() {
    private val autoCompletePublishSubject = PublishSubject.create<String>()
    private val compositeDisposable = CompositeDisposable()

    private val _suggestion = MutableLiveData<List<SearchSuggestionRecyclerViewAdapter.Item>>()
    val suggestion: LiveData<List<SearchSuggestionRecyclerViewAdapter.Item>> = _suggestion
    val hasSuggestion: LiveData<Boolean> = Transformations.map(suggestion) { !it.isEmpty() }

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    init {
        // Show saved suggestions at first
        repository.getSuggestions("")
            .subscribeOn(Schedulers.io())
            .subscribe({ suggestions ->
                _suggestion.postValue(suggestions)
            }, Timber::e)
            .also { compositeDisposable.add(it) }

        autoCompletePublishSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap { repository.getSuggestions(it).toObservable() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _suggestion.postValue(it)
            }, { error ->
                Timber.e(error)
            }).also { compositeDisposable.add(it) }
    }

    fun requestSuggestion(query: String) {
        autoCompletePublishSubject.onNext(query)
    }

    fun clearSuggestion() {
        _suggestion.postValue(emptyList())
    }

    fun startSearch(searchQuery: String) {
        repository.rememberQuery(searchQuery)
        clearSuggestion()
        _searchQuery.postValue(searchQuery)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    class Factory(
        private val repository: HistorySearchSuggestionRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                SearchViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unknown view model class.")
            }
    }
}
