package com.ishikota.photoviewerandroid.ui.userdetail.postedphotos

import android.content.Context
import android.os.Bundle
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.ui.userdetail.photolist.UserDetailPhotoListFragment

class UserDetailPostedPhotosFragment : UserDetailPhotoListFragment(), TabElement {

    override val title: Int? = R.string.posted

    override val iconResId: Int? = null

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
