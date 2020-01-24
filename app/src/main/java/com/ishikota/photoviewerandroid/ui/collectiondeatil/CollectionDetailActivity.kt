package com.ishikota.photoviewerandroid.ui.collectiondeatil

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.PhotodetailActivityBinding

class CollectionDetailActivity : AppCompatActivity() {

    private lateinit var binding: PhotodetailActivityBinding

    private val collectionId: String by lazy { intent.getStringExtra(EXTRA_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.photodetail_activity)

        val args = CollectionDetailFragmentArgs(collectionId)
        findNavController(R.id.nav_host_fragment)
            .setGraph(R.navigation.collectiondetail_nav_graph, args.toBundle())

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
        private const val EXTRA_ID = "collection_id"
        fun createIntent(context: Context, photoId: String): Intent =
            Intent(context, CollectionDetailActivity::class.java).apply {
                putExtra(EXTRA_ID, photoId)
            }
    }
}
