package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.PhotoLikeResult
import io.reactivex.Single

class PhotoRepositoryImpl(private val photoViewerService: PhotoViewerService) : PhotoRepository {

    override fun getPhotos(page: Int, order: PhotoRepository.Order): Single<List<Photo>> =
        photoViewerService.getPhotos(page = page, perPage = PER_PAGE, orderBy = order.value)

    override fun getPhoto(id: String): Single<Photo> = photoViewerService.getPhoto(id)

    override fun likePhoto(id: String): Single<PhotoLikeResult> =
        photoViewerService.likePhoto(id)

    override fun unLikePhoto(id: String): Single<PhotoLikeResult> =
        photoViewerService.unLikePhoto(id)

    companion object {
        const val PER_PAGE = 30
    }
}
