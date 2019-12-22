package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import io.reactivex.Single

class PhotoRepositoryImpl(private val photoViewerService: PhotoViewerService) : PhotoRepository {

    override fun getPhotos(page: Int, order: PhotoRepository.Order): Single<List<Photo>> =
        photoViewerService.getPhotos(page = page, perPage = PER_PAGE, orderBy = order.value)

    companion object {
        const val PER_PAGE = 30
    }
}
