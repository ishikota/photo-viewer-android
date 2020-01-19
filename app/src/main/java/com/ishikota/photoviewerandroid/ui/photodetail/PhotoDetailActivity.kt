package com.ishikota.photoviewerandroid.ui.photodetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.PhotodetailActivityBinding

class PhotoDetailActivity : AppCompatActivity() {

    private lateinit var binding: PhotodetailActivityBinding

    private val photoId: String by lazy { intent.getStringExtra("photoId") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.photodetail_activity)

        val args = PhotoDetailFragmentArgs(photoId)
        findNavController(R.id.nav_host_fragment)
            .setGraph(R.navigation.photodetail_nav_graph, args.toBundle())

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        title = ""
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        super.onSupportNavigateUp()
        return true
    }

    companion object {
        fun createIntent(context: Context, photoId: String): Intent =
            Intent(context, PhotoDetailActivity::class.java).apply {
                putExtra("photoId", photoId)
            }
    }
}
