package com.ishikota.photoviewerandroid.ui.top

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.ui.top.collectionlist.TopCollectionListFragment
import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListFragment

class TopPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> TopPhotoListFragment()
        1 -> TopCollectionListFragment()
        else -> throw IllegalStateException("Unexpected position $position")
    }

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> context.getString(R.string.top_tab_photo)
        1 -> context.getString(R.string.top_tab_collection)
        else -> throw IllegalStateException("Unexpected position $position")
    }
}
