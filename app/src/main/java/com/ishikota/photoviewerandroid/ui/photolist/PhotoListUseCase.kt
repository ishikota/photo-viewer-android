package com.ishikota.photoviewerandroid.ui.photolist

import io.reactivex.Single

interface PhotoListUseCase<P> {
    fun execute(page: Int, params: P): Single<List<PhotoListAdapter.Item>>
}
