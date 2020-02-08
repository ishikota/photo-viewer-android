package com.ishikota.photoviewerandroid.ui.search

import dagger.Subcomponent

@Subcomponent(modules = [SearchModule::class, SearchViewModelModule::class])
interface SearchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): SearchComponent
    }

    fun inject(fragment: SearchFragment)
}
