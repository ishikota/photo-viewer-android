package com.ishikota.photoviewerandroid.ui.top

import android.content.Context
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
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.repository.OauthTokenRepository
import com.ishikota.photoviewerandroid.databinding.TopFragmentBinding
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.TabElement
import com.ishikota.photoviewerandroid.infra.attachTabLayoutAdapter
import com.ishikota.photoviewerandroid.ui.top.collectionlist.TopCollectionListFragment
import com.ishikota.photoviewerandroid.ui.top.photolist.TopPhotoListFragment
import javax.inject.Inject

class TopFragment : Fragment() {

    private lateinit var binding: TopFragmentBinding

    @Inject
    lateinit var oauthTokenRepository: OauthTokenRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().inject(this)
    }

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
            if (oauthTokenRepository.isLoggedIn()) {
                Toast.makeText(requireContext(), "TODO open account page", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_global_loginFragment)
            }
        }
        binding.menuSearch.setOnClickListener {
            val action = TopFragmentDirections.actionTopFragmentToSearchFragment(null)
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
