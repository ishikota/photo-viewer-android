package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import io.reactivex.Single

class CollectionRepositoryImpl(val photoViewerService: PhotoViewerService) : CollectionRepository {

    override fun getCollections(page: Int): Single<List<Collection>> =
        photoViewerService.getCollections(page, PER_PAGE)

    override fun getCollection(id: String): Single<Collection> =
        photoViewerService.getCollection(id)

    override fun getCollectionPhotos(id: String): Single<List<Photo>> =
        photoViewerService.getCollectionPhotos(id, 1, PER_PAGE)

    companion object {
        const val PER_PAGE = 10
    }
}
