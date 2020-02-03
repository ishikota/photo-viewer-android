package com.ishikota.photoviewerandroid.ui.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.databinding.TopFragmentBinding
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.attachTabLayoutAdapter
import com.ishikota.photoviewerandroid.ui.top.collectionlist.TopCollectionListFragment
import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListFragment

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

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(findNavController(this))

        binding.menuAccount.setOnClickListener {
            Toast.makeText(requireContext(), "TODO account", Toast.LENGTH_SHORT).show()
        }
        binding.menuSearch.setOnClickListener {
            val action = TopFragmentDirections.actionTopFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        val tabs = listOf<TabElement>(
            TopPhotoListFragment(),
            TopCollectionListFragment()
        )
        binding.viewPager.attachTabLayoutAdapter(tabs, childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}
