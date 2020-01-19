package com.ishikota.photoviewerandroid.ui.photodetail

import dagger.Subcomponent

@Subcomponent(modules = [PhotoDetailViewModelModule::class, PhotoDetailModule::class])
interface PhotoDetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PhotoDetailComponent
    }
    fun inject(fragment: PhotoDetailFragment)
}

