package com.triviallanguages.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.triviallanguages.R
import com.triviallanguages.databinding.ActivityLoginBinding
import com.triviallanguages.util.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class LoginActivity : AppCompatActivity(), LoginListener, KodeinAware,
    ActivityNavigation {

    override val kodein by kodein()
    private val factory : LoginViewModelFactory by instance()

    var viewModel: LoginViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, factory).get(LoginViewModel::class.java)
       // binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewModel?.loginListener = this
        viewModel?.startActivityForResultEvent?.setEventReceiver(this,this )
        viewModel?.getLoggedInUser()?.observe(this, Observer { user ->
            if(user != null){
                //Log.d("adadsda",user._id.toString())
                startHomeActivity()
            }
        })
    }

    override fun onStarted() {
        showLoadingDialog()
    }

    override fun onSuccess() {
        cancelLoading()
    }

    override fun onFailure(message: String) {
        cancelLoading()
        toast(message)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel?.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
   }

}