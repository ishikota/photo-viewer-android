package com.ishikota.photoviewerandroid.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ishikota.photoviewerandroid.infra.Event
import timber.log.Timber
import kotlin.concurrent.thread


class MainViewModel(private val arg: String) : ViewModel() {

    private val _items = MutableLiveData<List<String>>()
    val items: LiveData<List<String>>
        get() = _items

    private val _showAlertAction = MutableLiveData<Event<String>>()
    val showAlertAction: LiveData<Event<String>>
        get() = _showAlertAction

    init {
        Timber.d("ViewModel Argument=$arg")
        _showAlertAction.postValue(Event("ViewModel initialized!!"))
    }

    fun fetchItems() {
        thread {
            Thread.sleep(5000)
            Handler(Looper.getMainLooper()).post {
                _items.postValue(listOf("hoge", "fuga", "bar"))
                _showAlertAction.postValue(Event("Item fetched"))
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val arg: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(arg) as T
        }
    }
}
