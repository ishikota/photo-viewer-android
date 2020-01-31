package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotoListPageKeyedDataSource<P>(
    private val params: P,
    private val useCase: PhotoListUseCase<P>
) : NetworkStatePageKeyedDataSource<Int, PhotoListAdapter.Item>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<PhotoListAdapter.Item>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        useCase.execute(key, params)
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

    class Factory<P>(
        private val params: P,
        private val useCase: PhotoListUseCase<P>
    ) : DataSource.Factory<Int, PhotoListAdapter.Item>() {

        val sourceLiveData = MutableLiveData<PhotoListPageKeyedDataSource<P>>()

        override fun create(): DataSource<Int, PhotoListAdapter.Item> {
            val source = PhotoListPageKeyedDataSource(params, useCase)
            sourceLiveData.postValue(source)
            return source
        }
    }

}
