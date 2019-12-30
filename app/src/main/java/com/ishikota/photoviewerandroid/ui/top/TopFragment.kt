package com.ishikota.photoviewerandroid.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ishikota.photoviewerandroid.databinding.TopFragmentBinding
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.attachTabLayoutAdapter
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListFragment

class TopFragment : Fragment() {

    private lateinit var binding: TopFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TopFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabs = listOf<TabElement>(
            PhotoListFragment()
        )
        binding.viewPager.attachTabLayoutAdapter(tabs, requireFragmentManager())
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
