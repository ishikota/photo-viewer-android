package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import io.reactivex.Single

interface CollectionRepository {

    fun getCollections(page: Int): Single<List<Collection>>
}
