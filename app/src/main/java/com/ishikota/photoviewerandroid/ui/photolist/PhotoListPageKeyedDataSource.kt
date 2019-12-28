package com.ishikota.photoviewerandroid.ui.photolist

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.infra.moshi.buildDefaultMoshi
import com.ishikota.photoviewerandroid.infra.paging.NetworkStatePageKeyedDataSource
import com.squareup.moshi.Types
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PhotoListPageKeyedDataSource(
    private val listOrder: PhotoRepository.Order,
    private val photoRepository: PhotoRepository
) : NetworkStatePageKeyedDataSource<Int, Photo>() {

    private val compositeDisposable = CompositeDisposable()

    override fun loadItems(
        key: Int,
        perPage: Int,
        successCallback: (List<Photo>) -> Unit,
        failureCallback: (Throwable) -> Unit
    ) {
        photoRepository.getPhotos(key, listOrder)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { items ->
                    val s = modelsToJson(items)
                    Timber.d(s)
                    successCallback(items)
                },
                { throwable -> failureCallback(throwable) }
            )
            .also { compositeDisposable.add(it) }
    }

    inline fun<reified T> jsonToModels(json: String): List<T>? =
        buildDefaultMoshi().adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java)).fromJson(json)
    inline fun <reified T> modelsToJson(models: List<T>): String =
        buildDefaultMoshi().adapter<List<T>>(Types.newParameterizedType(List::class.java, T::class.java)).toJson(models)

    override fun getNextPageKey(current: Int?): Int = if (current == null) 1 else current + 1

    override fun clear() {
        compositeDisposable.clear()
    }

    class Factory(
        private val listOrder: PhotoRepository.Order,
        private val photoRepository: PhotoRepository
    ) : DataSource.Factory<Int, Photo>() {

        val sourceLiveData = MutableLiveData<PhotoListPageKeyedDataSource>()

        override fun create(): DataSource<Int, Photo> {
            val source = PhotoListPageKeyedDataSource(listOrder, photoRepository)
            sourceLiveData.postValue(source)
            return source
        }
    }

}
