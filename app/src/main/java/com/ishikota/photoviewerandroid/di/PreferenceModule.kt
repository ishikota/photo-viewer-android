package com.ishikota.photoviewerandroid.di

import android.content.Context
import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import com.ishikota.photoviewerandroid.data.PhotoViewerPreferenceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule(
    private val context: Context
) {

    @Singleton
    @Provides
    fun providePreference(): PhotoViewerPreference = PhotoViewerPreferenceImpl(
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    )

    companion object {
        const val PREF_NAME = "photoviewer_preference"
    }
}
