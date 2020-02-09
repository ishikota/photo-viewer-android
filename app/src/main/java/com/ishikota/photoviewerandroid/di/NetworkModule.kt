package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.BuildConfig
import com.ishikota.photoviewerandroid.data.api.PhotoViewerLoginService
import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.infra.flipper.FlipperWrapper
import com.ishikota.photoviewerandroid.infra.format
import com.ishikota.photoviewerandroid.infra.okhttp.ApiAccessKeyInterceptor
import com.ishikota.photoviewerandroid.infra.toOffsetDateTime
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule(
    private val apiEndpoint: String,
    private val oauthEndpoint: String,
    private val appAccessKey: String
) {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(DateAdapter())
        .build()

    @Singleton
    @Provides
    @OkHttpClientQualifier(OkHttpClientQualifier.Type.Default)
    fun provideDefaultOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            addInterceptor(FlipperWrapper.getOkHttpInterceptor())
        }
    }.build()

    @Singleton
    @Provides
    @OkHttpClientQualifier(OkHttpClientQualifier.Type.Api)
    fun provideApiOkHttpClient(
        @OkHttpClientQualifier(OkHttpClientQualifier.Type.Default) defaultOkHttpClient: OkHttpClient
    ): OkHttpClient = defaultOkHttpClient
        .newBuilder()
        .addInterceptor(ApiAccessKeyInterceptor(appAccessKey))
        .build()

    @Singleton
    @Provides
    fun providePhotoViewerService(
        moshi: Moshi,
        @OkHttpClientQualifier(OkHttpClientQualifier.Type.Api) okHttpClient: OkHttpClient
    ): PhotoViewerService {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiEndpoint)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(PhotoViewerService::class.java)
    }

    @Singleton
    @Provides
    fun providePhotoViewerLoginService(
        moshi: Moshi,
        @OkHttpClientQualifier(OkHttpClientQualifier.Type.Default) okHttpClient: OkHttpClient
    ): PhotoViewerLoginService {
        val retrofit = Retrofit.Builder()
            .baseUrl(oauthEndpoint)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(PhotoViewerLoginService::class.java)
    }
}

private class DateAdapter {
    @ToJson
    fun toJson(date: OffsetDateTime): String = date.format()

    @FromJson
    fun fromJson(json: String): OffsetDateTime = json.toOffsetDateTime()
}
