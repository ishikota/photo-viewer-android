package com.ishikota.photoviewerandroid.data.api

import com.ishikota.photoviewerandroid.data.api.entities.Photo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoViewerService {

    @GET("/photos")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<List<Photo>>

}
