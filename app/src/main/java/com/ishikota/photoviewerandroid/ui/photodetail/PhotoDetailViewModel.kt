package com.ishikota.photoviewerandroid.ui.photodetail

import androidx.lifecycle.*
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.repository.OauthTokenRepository
import com.ishikota.photoviewerandroid.data.repository.PhotoRepository
import com.ishikota.photoviewerandroid.infra.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class PhotoDetailViewModel @Inject constructor(
    private val oauthTokenRepository: OauthTokenRepository,
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val photo: MutableLiveData<Photo> = MutableLiveData()
    private val isLikeUpdating: MutableLiveData<Boolean> = MutableLiveData()

    private val uiModel = MediatorLiveData<PhotoDetailUiModel>().apply {
        addSource(photo) { refreshUiModel() }
        addSource(isLikeUpdating) { refreshUiModel() }
    }
    val recyclerViewData = Transformations.map(uiModel) { it.toRecyclerViewData() }

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError: MutableLiveData<Boolean> = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _likeWithoutLoginAlert: MutableLiveData<Event<Unit>> = MutableLiveData()
    val likeWithoutLoginAlert: LiveData<Event<Unit>> = _likeWithoutLoginAlert

    fun loadData(id: String) {
        photoRepository.getPhoto(id)
            .doOnSubscribe {
                _isError.postValue(false)
                _isLoading.postValue(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                photo.postValue(it)
                _isLoading.postValue(false)
            }, { error ->
                Timber.e(error)
                _isLoading.postValue(false)
                _isError.postValue(true)
            }).also { compositeDisposable.add(it) }
    }

    fun toggleLike() {
        if (!oauthTokenRepository.isLoggedIn()) {
            _likeWithoutLoginAlert.value = Event(Unit)
            return
        }

        val photoEntity = photo.value ?: return
        val updateLike = if (photoEntity.likedByUser) {
            photoRepository.unLikePhoto(photoEntity.id)
        } else {
            photoRepository.likePhoto(photoEntity.id)
        }
        updateLike
            .doOnSubscribe {
                isLikeUpdating.postValue(true)
            }
            .doFinally {
                isLikeUpdating.postValue(false)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                // Update like state of photo LiveData
                photo.postValue(
                    photoEntity.copy(
                        likedByUser = it.photo.likedByUser, likes = it.photo.likes
                    )
                )
            }, { error ->
                Timber.e(error)
            }).also { compositeDisposable.add(it) }
    }

    private fun refreshUiModel() {
        val photoEntity = photo.value ?: return
        val newModel = uiModel.value ?: PhotoDetailUiModel(photoEntity)
        newModel.photo = photoEntity
        newModel.isLikeUpdating = isLikeUpdating.value ?: false
        uiModel.postValue(newModel)
    }
}
