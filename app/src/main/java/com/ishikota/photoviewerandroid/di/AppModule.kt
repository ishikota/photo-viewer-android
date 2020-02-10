package com.ishikota.photoviewerandroid.di

import com.ishikota.photoviewerandroid.BuildConfig
import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import com.ishikota.photoviewerandroid.data.api.PhotoViewerLoginService
import com.ishikota.photoviewerandroid.data.api.PhotoViewerService
import com.ishikota.photoviewerandroid.data.repository.*
import com.ishikota.photoviewerandroid.ui.login.LoginFragment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesCollectionRepository(
        service: PhotoViewerService
    ): CollectionRepository {
        return CollectionRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun providePhotoRepository(
        service: PhotoViewerService
    ): PhotoRepository {
        return PhotoRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        service: PhotoViewerService
    ): UserRepository {
        return UserRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        service: PhotoViewerService
    ): SearchRepository {
        return SearchRepositoryImpl(service)
    }

    @Singleton
    @Provides
    fun provideOauthTokenRepository(
        service: PhotoViewerLoginService,
        preference: PhotoViewerPreference
    ): OauthTokenRepository {
        return OauthTokenRepositoryImpl(
            service,
            preference,
            BuildConfig.APP_ACCESS_KEY,
            BuildConfig.APP_SECRET_KEY,
            LoginFragment.REDIRECT_URI
        )
    }
}
