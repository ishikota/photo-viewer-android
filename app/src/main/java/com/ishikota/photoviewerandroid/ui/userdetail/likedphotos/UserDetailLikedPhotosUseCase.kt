package com.ishikota.photoviewerandroid.ui.userdetail.likedphotos

import com.ishikota.photoviewerandroid.data.repository.UserRepository
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListUseCase
import io.reactivex.Single

class UserDetailLikedPhotosUseCase(
    private val userRepository: UserRepository
) : PhotoListUseCase<String> {

    // params == username
    override fun execute(page: Int, params: String): Single<List<PhotoListAdapter.Item>> =
        userRepository.getUserLikedPhotos(params, page).map { photos ->
            photos.map { PhotoListAdapter.Item.PhotoItem(it) }
        }
}
