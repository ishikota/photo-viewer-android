package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotoListPageKeyedDataSource(
    private val listOrder: PhotoRepository.Order,
    private val useCase: LoadPhotoListUseCase
) : NetworkStatePageKeyedDataSource<Int, PhotoListAdapter.Item>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<PhotoListAdapter.Item>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        useCase.execute(key, listOrder)
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
        private val listOrder: PhotoRepository.Order,
        private val useCase: LoadPhotoListUseCase
    ) : DataSource.Factory<Int, PhotoListAdapter.Item>() {

        val sourceLiveData = MutableLiveData<PhotoListPageKeyedDataSource>()

        override fun create(): DataSource<Int, PhotoListAdapter.Item> {
            val source = PhotoListPageKeyedDataSource(listOrder, useCase)
            sourceLiveData.postValue(source)
            return source
        }
    }

}
