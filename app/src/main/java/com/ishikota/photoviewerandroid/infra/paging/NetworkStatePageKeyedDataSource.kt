package com.ishikota.photoviewerandroid.infra.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource

abstract class NetworkStatePageKeyedDataSource<Key, Value>: PageKeyedDataSource<Key, Value>() {

    private var retry: (() -> Any)? = null

    private val _networkState = MutableLiveData<PagingNetworkState>()
    val networkState: LiveData<PagingNetworkState> = _networkState

    private val _initialLoad = MutableLiveData<PagingNetworkState>()
    val initialLoad: LiveData<PagingNetworkState> = _initialLoad

    fun retry() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Key>, callback: LoadInitialCallback<Key, Value>) {
        _initialLoad.postValue(PagingNetworkState.LOADING)
        val currentPageKey = getNextPageKey(null)
        val nextPageKey = getNextPageKey(currentPageKey)
        loadItems(
            key = currentPageKey,
            perPage = params.requestedLoadSize,
            successCallback = { items ->
                callback.onResult(items, null, nextPageKey)
                _initialLoad.postValue(PagingNetworkState.LOADED)
            },
            failureCallback = { throwable ->
                retry = { loadInitial(params, callback) }
                _initialLoad.postValue(PagingNetworkState.error(throwable.message))
            }
        )
    }

    override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        _networkState.postValue(PagingNetworkState.LOADING)
        val currentPageKey = params.key
        val nextPageKey = getNextPageKey(currentPageKey)
        loadItems(
            key = currentPageKey,
            perPage = params.requestedLoadSize,
            successCallback = { items ->
                callback.onResult(items, nextPageKey)
                _networkState.postValue(PagingNetworkState.LOADED)
            },
            failureCallback = { throwable ->
                retry = { loadAfter(params, callback) }
                _networkState.postValue(PagingNetworkState.error(throwable.message))
            }
        )
    }

    override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        // do nothing
    }

    /**
     * Override and define your logic to load items.
     * If you success to load items, pass that result to "successCallback",
     * else invoke "errorCallback" with your error.
     */
    abstract fun loadItems(key: Key, perPage: Int, successCallback: (List<Value>) -> Unit, failureCallback: (Throwable) -> Unit)

    /**
     * Override and define your paging key logic.
     * If "current" is null, return key of first page,
     * else return next key of "current".
     */
    abstract fun getNextPageKey(current: Key?): Key

    /**
     * This method is called when ViewModel#clear is invoked.
     * You should clean up resources you created in your loading logic.
     */
    open fun clear() {
        // do nothing in default
    }
}
