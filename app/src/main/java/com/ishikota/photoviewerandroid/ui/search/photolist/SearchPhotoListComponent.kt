package com.ishikota.photoviewerandroid.ui.search.photolist

import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListFragment
import dagger.Subcomponent

@Subcomponent(modules = [SearchPhotoListModule::class])
interface SearchPhotoListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchPhotoListComponent
    }
    fun inject(fragment: SearchPhotoListFragment)
}
