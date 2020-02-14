package com.ishikota.photoviewerandroid.ui.userdetail.likedphotos

import android.content.Context
import android.os.Bundle
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailPhotoListFragment

class UserDetailLikedPhotosFragment : UserDetailPhotoListFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().userDetailLikedPhotosComponent().create().inject(this)
    }

    companion object {
        fun createInstance(userName: String) = UserDetailLikedPhotosFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_USER_NAME, userName)
            }
        }
    }
}
