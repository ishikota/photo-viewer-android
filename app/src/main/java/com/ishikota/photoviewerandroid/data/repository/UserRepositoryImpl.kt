package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import io.reactivex.Single

class UserRepositoryImpl(private val photoViewerService: PhotoViewerService) : UserRepository {

    override fun getUser(userName: String): Single<User> = photoViewerService.getUser(userName)

    override fun getUserPostedPhotos(userName: String, page: Int) =
        photoViewerService.getUserPostedPhotos(userName, page, PER_PAGE, ORDER_BY)

    override fun getUserLikedPhotos(userName: String, page: Int): Single<List<Photo>> =
        photoViewerService.getUserLikedPhotos(userName, page, PER_PAGE, ORDER_BY)

    override fun getUserCollections(userName: String, page: Int): Single<List<Collection>> =
        photoViewerService.getUserCollections(userName, page, PER_PAGE)

    override fun getMe(): Single<User> = photoViewerService.getMe()

    override fun putMe(
        firstName: String,
        lastName: String,
        location: String,
        bio: String
    ): Single<User> = photoViewerService.putMe(firstName, lastName, location, bio)

    companion object {
        const val PER_PAGE = 10
        const val ORDER_BY = "latest"
    }
}
