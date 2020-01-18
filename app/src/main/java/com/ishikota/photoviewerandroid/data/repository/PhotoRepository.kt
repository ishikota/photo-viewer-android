package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.entities.Photo
import io.reactivex.Single

interface PhotoRepository {

    enum class Order(val value: String) {
        LATEST("latest"),
        OLDEST("oldest"),
        POPULAR("popular")
    }

    fun getPhotos(page: Int, order: Order): Single<List<Photo>>

    fun getPhoto(id: String): Single<Photo>
}
