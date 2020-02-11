package com.ishikota.photoviewerandroid.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ishikota.photoviewerandroid.BuildConfig
import com.ishikota.photoviewerandroid.R

class LoginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(requireContext(), R.string.login_request_message, Toast.LENGTH_SHORT).show()

        val uri = Uri.parse("https://unsplash.com/oauth/authorize?scope=$SCOPE").buildUpon()
            .appendQueryParameter("client_id", BuildConfig.APP_ACCESS_KEY)
            .appendQueryParameter("redirect_uri", REDIRECT_URI)
            .appendQueryParameter("response_type", "code")
            .build()
        startActivity(Intent(Intent.ACTION_VIEW, uri))
        findNavController().popBackStack()
    }

    companion object {
        const val REDIRECT_URI = "ishikota://mysite.com/callback"
        private const val SCOPE = "public+read_user+write_user+write_likes"
    }
}
