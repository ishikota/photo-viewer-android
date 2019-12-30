package com.ishikota.photoviewerandroid.infra

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

interface TabElement {
    val title: Int?
    val iconResId: Int?
}

fun TabLayout.setupIconTabs(tabs: List<TabElement>, colorNormal: Int, colorSelected: Int) {
    for ((idx, tab) in tabs.withIndex()) {
        tab.iconResId?.let { getTabAt(idx)?.setIcon(it) }
    }
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.icon?.applyTint(context, colorSelected)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
            tab?.icon?.applyTint(context, colorSelected)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            tab?.icon?.applyTint(context, colorNormal)
        }

    })
    getTabAt(0)?.select()  // Select first tab explicitly to apply our tab design
}

fun ViewPager.attachTabLayoutAdapter(tabs: List<TabElement>, fragmentManager: FragmentManager) {
    adapter = object : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = tabs[position] as Fragment

        override fun getCount() = tabs.size

        override fun getPageTitle(position: Int): CharSequence? {
            val titleResId = tabs[position].title
            return if (titleResId != null) context.getString(titleResId) else null
        }
    }
}

@Suppress("DEPRECATION")
private fun Drawable.applyTint(context: Context, color: Int) =
    setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
