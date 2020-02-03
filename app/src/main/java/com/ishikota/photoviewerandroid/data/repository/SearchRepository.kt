package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.entities.SearchCollections
import com.ishikota.photoviewerandroid.data.api.entities.SearchPhotos
import com.ishikota.photoviewerandroid.data.api.entities.SearchUsers
import io.reactivex.Single

interface SearchRepository {
    fun searchPhotos(query: String, page: Int): Single<SearchPhotos>
    fun searchCollections(query: String, page: Int): Single<SearchCollections>
    fun searchUsers(query: String, page: Int): Single<SearchUsers>
}
