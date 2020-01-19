package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import io.reactivex.Single

interface CollectionRepository {

    fun getCollections(page: Int): Single<List<Collection>>

    fun getCollection(id: String): Single<Collection>

    fun getCollectionPhotos(id: String, page: Int): Single<List<Photo>>
}
