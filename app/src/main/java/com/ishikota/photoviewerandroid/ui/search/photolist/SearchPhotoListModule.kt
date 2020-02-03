package com.ishikota.photoviewerandroid.ui.search.photolist

import com.ishikota.photoviewerandroid.data.repository.SearchRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListPagingRepository
import dagger.Module
import dagger.Provides

@Module
class SearchPhotoListModule {
    @Provides
    fun providePhotoListPagingRepository(
        searchRepository: SearchRepository
    ): PhotoListPagingRepository<String> =
        PhotoListPagingRepository(
            SearchPhotoListUseCase(searchRepository)
        )
}
