package com.ishikota.photoviewerandroid.ui.collectionlist

import dagger.Subcomponent

@Subcomponent(modules = [CollectionListViewModelModule::class, CollectionListModule::class])
interface CollectionListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CollectionListComponent
    }
    fun inject(fragment: CollectionListFragment)
}
