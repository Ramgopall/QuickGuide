package com.triviallanguages.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.triviallanguages.R
import com.triviallanguages.databinding.ActivitySplashBinding
import com.triviallanguages.util.startHomeActivity
import com.triviallanguages.util.startLoginActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SplashActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    private val factory: LoginViewModelFactory by instance()

    var viewModel: LoginViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
        binding.viewmodel = viewModel
        Handler().postDelayed({
            viewModel?.getLoggedInUser()?.observe(this, Observer { user ->
                if (user != null) {
                    startHomeActivity()
                }
                else{
                    startLoginActivity()
                }
            })
        }, 1500)
    }
}
