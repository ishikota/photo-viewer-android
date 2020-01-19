package com.ishikota.photoviewerandroid.ui.collectiondeatil

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CollectionDetailPageKeyedDataSource(
    private val collectionId: String,
    private val useCase: CollectionDetailUseCase
) : NetworkStatePageKeyedDataSource<Int, CollectionDetailAdapter.Item>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<CollectionDetailAdapter.Item>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        useCase.execute(collectionId, key)
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
        private val collectionId: String,
        private val userCase: CollectionDetailUseCase
    ) : DataSource.Factory<Int, CollectionDetailAdapter.Item>() {

        val sourceLiveData = MutableLiveData<CollectionDetailPageKeyedDataSource>()

        override fun create(): DataSource<Int, CollectionDetailAdapter.Item> {
            val source = CollectionDetailPageKeyedDataSource(collectionId, userCase)
            sourceLiveData.postValue(source)
            return source
        }
    }
}
