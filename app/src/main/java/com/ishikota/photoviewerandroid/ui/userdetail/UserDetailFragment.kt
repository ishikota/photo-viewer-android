package com.ishikota.photoviewerandroid.ui.userdetail

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.UserdetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.attachTabLayoutAdapter
import com.ishikota.photoviewerandroid.ui.userdetail.likedphotos.UserDetailLikedPhotosFragment
import com.ishikota.photoviewerandroid.ui.userdetail.postedphotos.UserDetailPostedPhotosFragment
import javax.inject.Inject

open class UserDetailFragment : Fragment() {

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
        setHasOptionsMenu(true)
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
                binding.toolbar.title = safeArgs.user.name
            } else {
                binding.toolbar.title = ""
            }
        })

        binding.userdetailContents.retryButton.setOnClickListener {
            viewModel.loadUserDetail(safeArgs.user.userName)
        }

        viewModel.userDetail.observe(this, NonNullObserver {
            binding.user = it
            val tabs = mutableListOf<TabElement>()
            if (it.totalPhotos != 0) {
                tabs.add(UserDetailPostedPhotosFragment.createInstance(it.userName))
            }
            if (it.totalLikes != 0) {
                tabs.add(UserDetailLikedPhotosFragment.createInstance(it.userName))
            }
            binding.viewPager.attachTabLayoutAdapter(tabs, childFragmentManager)
            binding.tabLayout.setupWithViewPager(binding.viewPager)
        })

        viewModel.loadUserDetail(safeArgs.user.userName)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (safeArgs.allowEdit) {
            inflater.inflate(R.menu.user_detail_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editProfileFragment -> {
                // TODO
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
