package com.ishikota.photoviewerandroid.ui.userdetail.postedphotos

import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.userdetail.photolist.UserDetailPhotoListUseCase
import io.reactivex.Single

class UserDetailPostedPhotosUseCase(
    private val userRepository: UserRepository
) : UserDetailPhotoListUseCase {
    override fun execute(userName: String, page: Int): Single<List<PhotoListAdapter.Item>> =
        userRepository.getUserPostedPhotos(userName, page).map { photos ->
            photos.map { PhotoListAdapter.Item.PhotoItem(it) }
        }
}
