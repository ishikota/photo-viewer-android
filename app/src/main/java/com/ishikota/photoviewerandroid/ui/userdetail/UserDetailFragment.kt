package com.ishikota.photoviewerandroid.ui.userdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.ishikota.photoviewerandroid.databinding.UserdetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.attachTabLayoutAdapter
import com.ishikota.photoviewerandroid.ui.collectionlist.CollectionListFragment
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListFragment
import javax.inject.Inject

class UserDetailFragment: Fragment() {

    private val safeArgs: UserDetailFragmentArgs by navArgs()

    private lateinit var binding: UserdetailFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: UserDetailViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(UserDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().userDetailComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserdetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(NavHostFragment.findNavController(this))

        binding.toolbar.title = ""
        binding.collapsingtoolbarlayout.isTitleEnabled = false
        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            if (i == -appBarLayout.totalScrollRange) {   // if completely collapsed
                binding.toolbar.title = safeArgs.name
            } else {
                binding.toolbar.title = ""
            }
        })

        binding.userdetailContents.retryButton.setOnClickListener {
            viewModel.loadUserDetail(safeArgs.username)
        }

        viewModel.userDetail.observe(this, NonNullObserver {
            binding.user = it
            val tabs = listOf<TabElement>(
                PhotoListFragment(),
                CollectionListFragment()
            )
            binding.viewPager.attachTabLayoutAdapter(tabs, childFragmentManager)
            binding.tabLayout.setupWithViewPager(binding.viewPager)
        })

        viewModel.loadUserDetail(safeArgs.username)
    }

}
