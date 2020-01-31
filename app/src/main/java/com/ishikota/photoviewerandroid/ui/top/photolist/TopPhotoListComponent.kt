package com.ishikota.photoviewerandroid.ui.top.photolist

import dagger.Subcomponent

@Subcomponent(modules = [TopPhotoListModule::class])
interface TopPhotoListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TopPhotoListComponent
    }
    fun inject(fragment: TopPhotoListFragment)
}
