package com.ishikota.photoviewerandroid.data.api

import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoViewerService {

    @GET("/photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<List<Photo>>


    @GET("/photos/{id}")
    fun getPhoto(
        @Path("id") id: String
    ): Single<Photo>

    @GET("/collections")
    fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Collection>>

    @GET("/collections/{id}")
    fun getCollection(
        @Path("id") id: String
    ): Single<Collection>

    @GET("/collections/{id}/photos")
    fun getCollectionPhotos(
        @Path("id") id: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Photo>>

    @GET("/users/{username}")
    fun getUser(
        @Path("username") userName: String
    ): Single<User>

    @GET("/users/{username}/photos")
    fun getUserPostedPhotos(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<List<Photo>>

    @GET("/users/{username}/likes")
    fun getUserLikedPhotos(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<List<Photo>>

    @GET("/users/{username}/collections")
    fun getUserCollections(
        @Path("username") userName: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<List<Collection>>

}
