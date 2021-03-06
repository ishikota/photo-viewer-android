package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.ishikota.photoviewerandroid.data.api.entities.Collection
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.data.api.entities.User
import com.ishikota.photoviewerandroid.databinding.CollectiondetailFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import javax.inject.Inject

class CollectionDetailFragment : Fragment() {

    private val safeArgs: CollectionDetailFragmentArgs by navArgs()

    private lateinit var binding: CollectiondetailFragmentBinding

    private lateinit var adapter: CollectionDetailAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CollectionDetailViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(CollectionDetailViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().collectionDetailComponent().create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CollectiondetailFragmentBinding.inflate(inflater, container, false)
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
                binding.toolbar.title = safeArgs.collectionTitle
            } else {
                binding.toolbar.title = ""
            }
        })

        binding.collectionContents.userThumbnail.setOnClickListener {
            viewModel.collectionDetail.value?.user?.let {
                navigateToUserDetail(it)
            }
        }
        binding.collectionContents.coverImage.setOnClickListener {
            viewModel.collectionDetail.value?.coverPhoto?.let {
                navigateToPhotoDetail(it)
            }
        }
        binding.collectionContents.retryButton.setOnClickListener {
            viewModel.loadCollectionDetail(safeArgs.collectionId)
        }

        adapter = CollectionDetailAdapter(
            onPhotoClicked = this::navigateToPhotoDetail,
            onRelatedCollectionClicked = this::navigateToRelatedCollection,
            retryCallback = { viewModel.retry() }
        )
        binding.recyclerView.adapter = adapter

        viewModel.collectionDetail.observe(this, NonNullObserver {
            binding.collection = it
            binding.collectionContents.collectionContentGroup.visibility = View.VISIBLE
        })

        viewModel.pagedList.observe(this, NonNullObserver {
            adapter.submitList(it)
        })
        viewModel.initialLoadNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
            // TODO Workaround because RecyclerView is scrolled to last item of first page after initial load
            binding.recyclerView.scrollToPosition(0)
        })
        viewModel.loadMoreNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
        })

        viewModel.loadCollectionDetail(safeArgs.collectionId)
        viewModel.loadCollectionPhotos(safeArgs.collectionId)
    }

    private fun navigateToPhotoDetail(photo: Photo) {
        val action = CollectionDetailFragmentDirections.
            actionCollectionDetailFragmentToPhotoDetailFragment(photo.id)
        findNavController().navigate(action)
    }

    private fun navigateToUserDetail(user: User) {
        val action = CollectionDetailFragmentDirections.
            actionCollectionDetailFragmentToUserDetailFragment(user = user)
        findNavController().navigate(action)
    }

    private fun navigateToRelatedCollection(collection: Collection) {
        val action = CollectionDetailFragmentDirections
            .actionCollectionDetailFragmentSelf(collection.id, collection.title)
        findNavController().navigate(action)
    }
}
