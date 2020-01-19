package com.ishikota.photoviewerandroid.ui.collectiondeatil

import dagger.Subcomponent

@Subcomponent(modules = [CollectionDetailViewModelModule::class, CollectionDetailModule::class])
interface CollectionDetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CollectionDetailComponent
    }
    fun inject(fragment: CollectionDetailFragment)
}
