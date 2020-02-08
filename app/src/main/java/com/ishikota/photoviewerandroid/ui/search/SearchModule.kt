package com.ishikota.photoviewerandroid.ui.search

import androidx.lifecycle.ViewModel
import com.ishikota.photoviewerandroid.data.PhotoViewerPreference
import com.ishikota.photoviewerandroid.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SearchModule {
    @Provides
    fun provideSearchSuggestionRepository(
        preference: PhotoViewerPreference
    ): HistorySearchSuggestionRepository = HistorySearchSuggestionRepository(preference)
}

@Module
abstract class SearchViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}
