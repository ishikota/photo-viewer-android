package com.ishikota.photoviewerandroid.ui.search.photolist

import com.ishikota.photoviewerandroid.data.repository.SearchRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListUseCase
import io.reactivex.Single

class SearchPhotoListUseCase(
    private val searchRepository: SearchRepository
) : PhotoListUseCase<String> {
    override fun execute(page: Int, params: String): Single<List<PhotoListAdapter.Item>> =
        searchRepository.searchPhotos(params, page).map {
            it.results.map {
                PhotoListAdapter.Item.PhotoItem(it)
            }
        }
}
