package com.ishikota.photoviewerandroid.data.api

import com.ishikota.photoviewerandroid.data.api.entities.*
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import io.reactivex.Single
import retrofit2.http.*

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

    @POST("/photos/{id}/like")
    fun likePhoto(
        @Path("id") id: String
    ): Single<PhotoLikeResult>

    @DELETE("/photos/{id}/like")
    fun unLikePhoto(
        @Path("id") id: String
    ): Single<PhotoLikeResult>

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

    @GET("/collections/{id}/related")
    fun getCollectionsRelatedCollections(
        @Path("id") id: String
    ): Single<List<Collection>>

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

    @GET("/search/photos")
    fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<SearchPhotos>

    @GET("/search/collections")
    fun searchCollections(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<SearchCollections>

    @GET("/search/users")
    fun searchUsers(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<SearchUsers>

    @GET("/me")
    fun getMe(): Single<User>

    @PUT("/me")
    fun putMe(
        @Query("first_name") fistName: String?,
        @Query("last_name") lastName: String?,
        @Query("location") location: String?,
        @Query("bio") bio: String?
    ): Single<User>
}
