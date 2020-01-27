package com.ishikota.photoviewerandroid.ui.userdetail.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.ishikota.photoviewerandroid.data.api.entities.Photo
import com.ishikota.photoviewerandroid.databinding.UserdetailPhotolistFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.paging.PagingNetworkState
import com.ishikota.photoviewerandroid.infra.paging.Status
import com.ishikota.photoviewerandroid.ui.photolist.PhotoListAdapter
import com.ishikota.photoviewerandroid.ui.userdetail.UserDetailFragmentDirections
import javax.inject.Inject

open class UserDetailPhotoListFragment: Fragment() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME)!! }

    private lateinit var binding: UserdetailPhotolistFragmentBinding

    private lateinit var adapter: PhotoListAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: UserDetailPhotoListViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(UserDetailPhotoListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserdetailPhotolistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        adapter = PhotoListAdapter(
            retryCallback = { viewModel.retry() },
            onPhotoClicked = this::navigateToPhotoDetail,
            onOrderChangeRequested = { /* Never called in this fragment */ },
            onGridChangeRequested = { /* Never called in this fragment */ }
        )
        binding.recyclerView.adapter = adapter

        viewModel.pagedList.observe(this, NonNullObserver {
            adapter.submitList(it)
        })
        viewModel.initialLoadNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
            // TODO Workaround because RecyclerView is scrolled to last item of first page after initial load
            binding.recyclerView.scrollToPosition(0)
            // Hide adapter's progress while swipe refreshing
            if (binding.swipeRefresh.isRefreshing && it.status == Status.RUNNING) {
                adapter.setNetworkState(PagingNetworkState.LOADED)
            }
        })
        viewModel.loadMoreNetworkState.observe(this, NonNullObserver {
            adapter.setNetworkState(it)
        })

        viewModel.setUserName(userName)
    }

    private fun navigateToPhotoDetail(photo: Photo) {
        val action =
            UserDetailFragmentDirections.actionUserDetailFragmentToPhotoDetailFragment(photo.id)
        findNavController().navigate(action)
    }

    companion object {
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }
}
