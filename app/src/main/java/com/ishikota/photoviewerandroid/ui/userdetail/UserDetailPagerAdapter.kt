package com.ishikota.photoviewerandroid.ui.userdetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.ui.userdetail.likedphotos.UserDetailLikedPhotosFragment
import com.ishikota.photoviewerandroid.ui.userdetail.postedphotos.UserDetailPostedPhotosFragment

class UserDetailPagerAdapter(
    private val userName: String,
    private val contents: List<Contents>,
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    enum class Contents {
        POSTED_PHOTOS,
        LIKED_PHOTOS
    }

    override fun getCount(): Int = contents.size

    override fun getItem(position: Int): Fragment = when (contents[position]) {
        Contents.POSTED_PHOTOS -> UserDetailPostedPhotosFragment.createInstance(userName)
        Contents.LIKED_PHOTOS -> UserDetailLikedPhotosFragment.createInstance(userName)
    }

    override fun getPageTitle(position: Int): CharSequence? = when (contents[position]) {
        Contents.POSTED_PHOTOS -> context.getString(R.string.posted)
        Contents.LIKED_PHOTOS -> context.getString(R.string.liked)
    }
}
