package com.ishikota.photoviewerandroid.ui.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.EditProfileFragmentBinding
import com.ishikota.photoviewerandroid.di.ViewModelFactory
import com.ishikota.photoviewerandroid.di.appComponent
import com.ishikota.photoviewerandroid.infra.EventObserver
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import com.ishikota.photoviewerandroid.infra.dismissKeyboard
import com.ishikota.photoviewerandroid.ui.MainActivity
import javax.inject.Inject

class EditProfileFragment : Fragment() {

    private val safeArgs: EditProfileFragmentArgs by navArgs()

    private lateinit var binding: EditProfileFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: EditProfileViewModel by lazy {
        ViewModelProviders.of(
            this, viewModelFactory
        ).get(EditProfileViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent().editProfileComponent().create().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(NavHostFragment.findNavController(this))
        binding.toolbar.title = ""
        binding.userThumbnail.setOnClickListener {
            Toast.makeText(
                requireContext(),
                R.string.update_thumbnail_not_supported_message,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.user = safeArgs.placeholderUser

        viewModel.errorMessage.observe(this, EventObserver { messageId ->
            context?.let {
                Toast.makeText(it, messageId, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.updatedUser.observe(this, NonNullObserver {
            binding.user = it
        })
        viewModel.navigateAppRestartAction.observe(this, EventObserver {
            context?.let {
                Toast.makeText(it, R.string.logout_message, Toast.LENGTH_SHORT).show()
            }
            restartApp()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = EditProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.update_profile -> {
                viewModel.updateProfile(
                    firstName = binding.textFirstName.text.toString(),
                    lastName = binding.textLastName.text.toString(),
                    location = binding.textLocation.text.toString(),
                    bio = binding.bio.text.toString()
                )
                dismissKeyboard(activity, binding.bio)
                true
            }
            R.id.logout -> {
                viewModel.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun restartApp() {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(intent)
    }
}
