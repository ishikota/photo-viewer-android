package com.ishikota.photoviewerandroid.ui.photolist

import dagger.Subcomponent

@Subcomponent(modules = [PhotoListViewModelModule::class, PhotoListModule::class])
interface PhotoListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PhotoListComponent
    }
    fun inject(fragment: PhotoListFragment)
}
