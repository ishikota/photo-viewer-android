package com.ishikota.photoviewerandroid.ui.photodetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.databinding.PhotodetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import javax.inject.Inject

class PhotoDetailFragment: Fragment() {

    private val safeArgs: PhotoDetailFragmentArgs by navArgs()

    private lateinit var binding: PhotodetailFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var adapter: PhotoDetailAdapter

    private val viewModel: PhotoDetailViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(PhotoDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().photoDetailComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PhotodetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(NavHostFragment.findNavController(this))
        binding.toolbar.title = ""

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        adapter = PhotoDetailAdapter(
            onUserClicked = this::navigateToUserDetail,
            onLikeToggled = { viewModel.toggleLike() },
            onTagClicked = this::navigateToSearchFragment,
            onShareClicked = { photo ->
                Toast.makeText(
                    requireContext(),
                    "share clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
        binding.recyclerView.adapter = adapter
        binding.retryButton.setOnClickListener {
            viewModel.loadData(safeArgs.photoId)
        }

        viewModel.recyclerViewData.observe(this, NonNullObserver {
            adapter.submitList(it)
        })

        viewModel.loadData(safeArgs.photoId)
    }

    private fun navigateToUserDetail(user: User) {
        val action = PhotoDetailFragmentDirections.
            actionPhotoDetailFragmentToUserDetailFragment(user = user)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFragment(tag: Photo.Tag) {
        val action = PhotoDetailFragmentDirections.actionPhotoDetailFragmentToSearchFragment(tag.title)
        findNavController().navigate(action)
    }
}
