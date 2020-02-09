package com.ishikota.photoviewerandroid.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ishikota.photoviewerandroid.PhotoViewerApplication
import com.ishikota.photoviewerandroid.R
import com.ishikota.photoviewerandroid.data.repository.OauthTokenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class OauthCallbackActivity : AppCompatActivity() {

    @Inject
    lateinit var oauthTokenRepository: OauthTokenRepository

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        (application as PhotoViewerApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.oauth_callback_activity)

        Timber.d("onCreate called with ${intent.data}")
        val uri = intent.data
        val code = uri?.getQueryParameter("code")
        if (uri != null && uri.scheme.equals("ishikota") && code != null) {
            oauthTokenRepository.fetchAndSaveBearerToken(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showMessage(R.string.login_success_message)
                    finish()
                }, { error ->
                    Timber.e(error)
                    showMessage(R.string.login_failure_message)
                    finish()
                })
        } else {
            Timber.e("unexpected data received. data=${intent.data}")
            finish()
        }
    }

    private fun showMessage(id: Int) {
        Toast.makeText(applicationContext, id, Toast.LENGTH_SHORT).show()
    }
}
