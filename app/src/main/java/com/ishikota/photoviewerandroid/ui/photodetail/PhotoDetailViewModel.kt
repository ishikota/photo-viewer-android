package com.ishikota.photoviewerandroid.ui.photodetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PhotoDetailViewModel @Inject constructor(
    private val useCase: PhotoDetailUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val _recyclerViewData: MutableLiveData<List<PhotoDetailAdapter.Item>> =
        MutableLiveData()
    val recyclerViewData: LiveData<List<PhotoDetailAdapter.Item>> = _recyclerViewData

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    fun loadData(id: String) {
        useCase.execute(id)
            .doOnSubscribe {
                _isError.postValue(false)
                _isLoading.postValue(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _recyclerViewData.postValue(it)
                _isLoading.postValue(false)
            }, { error ->
                Timber.e(error)
                _isLoading.postValue(false)
                _isError.postValue(true)
            }).also { compositeDisposable.add(it) }
    }

    fun toggleLike() {
        // TODO call api
        val data = recyclerViewData.value?.toMutableList() ?: return
        val photoIndex = data.indexOfFirst { it is PhotoDetailAdapter.Item.PhotoItem }
        val photoItem = data[photoIndex] as PhotoDetailAdapter.Item.PhotoItem
        val isLiked = photoItem.entity.likedByUser

        val updated = photoItem.entity.copy(likedByUser = !isLiked)
        data[photoIndex] = PhotoDetailAdapter.Item.PhotoItem(updated)
        _recyclerViewData.postValue(data)
    }
}
