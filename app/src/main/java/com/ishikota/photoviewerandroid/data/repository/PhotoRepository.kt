package com.ishikota.photoviewerandroid.data.repository

import com.ishikota.photoviewerandroid.BuildConfig
import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.infra.moshi.buildDefaultMoshi
import com.ishikota.photoviewerandroid.infra.okhttp.ApiAccessKeyInterceptor
import com.ishikota.photoviewerandroid.infra.okhttp.buildDefaultOkHttpClient
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

interface PhotoRepository {

    enum class Order(val value: String) {
        LATEST("latest"),
        OLDEST("oldest"),
        POPULAR("popular")
    }

    fun getPhotos(page: Int, order: Order): Single<List<Photo>>

    object Factory {
        fun create() : PhotoRepository {
            val moshi = buildDefaultMoshi()
            val okHttpClient = buildDefaultOkHttpClient()
                .newBuilder()
                .addInterceptor(ApiAccessKeyInterceptor(BuildConfig.APP_ACCESS_KEY))
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            val service = retrofit.create(PhotoViewerService::class.java)
            return PhotoRepositoryImpl(service)
        }
    }
}
