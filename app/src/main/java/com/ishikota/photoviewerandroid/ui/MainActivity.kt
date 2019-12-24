package com.ishikota.photoviewerandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.databinding.MainActivityBinding
import com.ishikota.photoviewerandroid.infra.EventObserver
import com.ishikota.photoviewerandroid.infra.NonNullObserver
import timber.log.Timber
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, MainViewModel.Factory("Hello ViewModel!"))
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)

        viewModel.items.observe(this, NonNullObserver {
            Timber.d("item received=$it")
        })
        viewModel.showAlertAction.observe(this, EventObserver {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        thread {
            Thread.sleep(5000)
            viewModel.fetchItems()
        }
    }
}
