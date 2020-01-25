package com.ishikota.photoviewerandroid.ui.collectiondeatil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.repository.CollectionRepository
import com.ishikota.photoviewerandroid.infra.paging.PagingListing
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CollectionDetailViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val pagingRepository: CollectionDetailPagingRepository
) : ViewModel() {
    private val _collectionDetail = MutableLiveData<Collection>()
    val collectionDetail: LiveData<Collection> = _collectionDetail

    private val _isError = MutableLiveData<Boolean>(false)
    val isError: LiveData<Boolean> = _isError

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val listing = MutableLiveData<PagingListing<CollectionDetailAdapter.Item>>()
    val pagedList = Transformations.switchMap(listing) { it.pagedList }
    val initialLoadNetworkState = Transformations.switchMap(listing) { it.initialLoadNetworkState }
    val loadMoreNetworkState = Transformations.switchMap(listing) { it.loadMoreNetworkState }

    private val compositeDisposable = CompositeDisposable()

    fun loadCollectionDetail(id: String) {
        collectionRepository.getCollection(id)
            .doOnSubscribe {
                _isError.postValue(false)
                _isLoading.postValue(true)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ collection ->
                _collectionDetail.postValue(collection)
                _isLoading.postValue(false)
            }, { error ->
                Timber.e(error)
                _isLoading.postValue(false)
                _isError.postValue(true)
            }).also { compositeDisposable.add(it) }
    }

    fun loadCollectionPhotos(collectionId: String) {
        listing.postValue(pagingRepository.getListing(collectionId))
    }

    fun retry() {
        listing.value?.retry?.invoke()
    }

    override fun onCleared() {
        listing.value?.clear?.invoke()
    }
}
