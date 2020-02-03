package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.SearchCollections
import com.ishikota.photoviewerandroid.data.api.entities.SearchPhotos
import com.ishikota.photoviewerandroid.data.api.entities.SearchUsers
import io.reactivex.Single

class SearchRepositoryImpl(private val photoViewerService: PhotoViewerService): SearchRepository {
    override fun searchPhotos(query: String, page: Int): Single<SearchPhotos> =
        photoViewerService.searchPhotos(query, page, PER_PAGE)

    override fun searchCollections(query: String, page: Int): Single<SearchCollections> =
        photoViewerService.searchCollections(query, page, PER_PAGE)

    override fun searchUsers(query: String, page: Int): Single<SearchUsers> =
        photoViewerService.searchUsers(query, page, PER_PAGE)

    companion object {
        const val PER_PAGE = 10
    }
}
