package com.ishikota.photoviewerandroid.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setupWithNavController(findNavController(R.id.nav_host_fragment))

        binding.menuAccount.setOnClickListener {
            Toast.makeText(this, "TODO account", Toast.LENGTH_SHORT).show()
        }
        binding.menuSearch.setOnClickListener {
            Toast.makeText(this, "TODO search", Toast.LENGTH_SHORT).show()
        }
    }
}
