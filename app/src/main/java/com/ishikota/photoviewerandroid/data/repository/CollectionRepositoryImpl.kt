package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import io.reactivex.Single

class CollectionRepositoryImpl(val photoViewerService: PhotoViewerService) : CollectionRepository {

    override fun getCollections(page: Int): Single<List<Collection>> =
        photoViewerService.getCollections(page, PER_PAGE)

    companion object {
        const val PER_PAGE = 10
    }
}
