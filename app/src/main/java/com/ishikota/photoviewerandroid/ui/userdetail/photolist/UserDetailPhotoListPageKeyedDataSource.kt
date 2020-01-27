package com.ishikota.photoviewerandroid.ui.userdetail.photolist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserDetailPhotoListPageKeyedDataSource(
    private val userName: String,
    private val useCase: UserDetailPhotoListUseCase
) : NetworkStatePageKeyedDataSource<Int, PhotoListAdapter.Item>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<PhotoListAdapter.Item>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        useCase.execute(userName, key)
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

    class Factory(
        private val userName: String,
        private val useCase: UserDetailPhotoListUseCase
    ) : DataSource.Factory<Int, PhotoListAdapter.Item>() {

        val sourceLiveData = MutableLiveData<UserDetailPhotoListPageKeyedDataSource>()

        override fun create(): DataSource<Int, PhotoListAdapter.Item> {
            val source = UserDetailPhotoListPageKeyedDataSource(userName, useCase)
            sourceLiveData.postValue(source)
            return source
        }
    }
}
