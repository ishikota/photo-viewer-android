package com.ishikota.photoviewerandroid.ui.collectionlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CollectionListPageKeyedDataSource(
    private val collectionRepository: CollectionRepository
) : NetworkStatePageKeyedDataSource<Int, Collection>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<Collection>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        collectionRepository.getCollections(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { items ->
                    successCallback(items)
                },
                { throwable -> failureCallback(throwable) }
            )
            .also { compositeDisposable.add(it) }
    }

    override fun getNextPageKey(current: Int?): Int = if (current == null) 1 else current + 1

    override fun clear() {
        compositeDisposable.clear()
    }

    class Factory(
        private val repository: CollectionRepository
    ) : DataSource.Factory<Int, Collection>() {

        val sourceLiveData = MutableLiveData<CollectionListPageKeyedDataSource>()

        override fun create(): DataSource<Int, Collection> {
            val source = CollectionListPageKeyedDataSource(repository)
            sourceLiveData.postValue(source)
            return source
        }
    }
}
