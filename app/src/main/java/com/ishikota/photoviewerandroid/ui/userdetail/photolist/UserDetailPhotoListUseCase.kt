package com.ishikota.photoviewerandroid.ui.userdetail.photolist

import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import io.reactivex.Single

interface UserDetailPhotoListUseCase {
    fun execute(userName: String, page: Int): Single<List<PhotoListAdapter.Item>>
}
