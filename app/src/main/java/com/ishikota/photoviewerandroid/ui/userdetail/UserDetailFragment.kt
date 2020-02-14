package com.ishikota.photoviewerandroid.ui.userdetail

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.UserdetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import javax.inject.Inject

class UserDetailFragment : Fragment() {

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

        viewModel.userDetail.observe(this, NonNullObserver { user ->
            context?.let { context ->
                binding.user = user
                val contents = mutableListOf<UserDetailPagerAdapter.Contents>()
                if (user.totalPhotos != 0) {
                    contents.add(UserDetailPagerAdapter.Contents.POSTED_PHOTOS)
                }
                if (user.totalLikes != 0) {
                    contents.add(UserDetailPagerAdapter.Contents.LIKED_PHOTOS)
                }
                binding.viewPager.adapter =
                    UserDetailPagerAdapter(user.userName, contents, context, childFragmentManager)
                binding.tabLayout.setupWithViewPager(binding.viewPager)
            }
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
                val user = viewModel.userDetail.value
                if (user == null) {
                    Toast.makeText(
                        requireContext(),
                        R.string.user_detail_loading_message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val action = UserDetailFragmentDirections
                        .actionUserDetailFragmentToEditProfileFragment(user)
                    findNavController().navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
