package com.ishikota.photoviewerandroid.ui.top.collectionlist

import dagger.Subcomponent

@Subcomponent(modules = [TopCollectionListModule::class])
interface TopCollectionListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TopCollectionListComponent
    }
    fun inject(fragment: TopCollectionListFragment)
}
