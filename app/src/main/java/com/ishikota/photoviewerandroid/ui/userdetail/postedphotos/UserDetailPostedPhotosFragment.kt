package com.ishikota.photoviewerandroid.ui.userdetail.postedphotos

import android.content.Context
import android.os.Bundle
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailPhotoListFragment

class UserDetailPostedPhotosFragment : UserDetailPhotoListFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().userDetailPostedPhotosComponent().create().inject(this)
    }

    companion object {
        fun createInstance(userName: String) = UserDetailPostedPhotosFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_USER_NAME, userName)
            }
        }
    }
}
