package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import io.reactivex.Single

interface UserRepository {
    fun getUser(userName: String): Single<User>
    fun getUserPostedPhotos(userName: String, page: Int): Single<List<Photo>>
    fun getUserLikedPhotos(userName: String, page: Int): Single<List<Photo>>
    fun getUserCollections(userName: String, page: Int): Single<List<Collection>>
    fun getMe(): Single<User>
}
