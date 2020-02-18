package com.duke.diskoverusosyal.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.duke.diskoverusosyal.R
import com.duke.diskoverusosyal.model.login.LoginModel
import com.duke.diskoverusosyal.retrofitData.ApiClient
import com.duke.diskoverusosyal.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private val PERMISSION_CALLBACK_CONSTANT = 100
    var permissionsRequired = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    var mGoogleApiClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        Twitter.initialize(this)
        setContentView(R.layout.activity_login)
        runtimePermission()
        LoginButtonClick()
        ForgotPassword()
        RegisterNow()
        twitterLogin()
        googleLogin()
    }

    private fun runtimePermission(){
        // Multiple Runtime Permission
        if (ActivityCompat.checkSelfPermission(this@LoginActivity, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@LoginActivity, permissionsRequired[0])) {
                ActivityCompat.requestPermissions(this@LoginActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            } else {
                ActivityCompat.requestPermissions(this@LoginActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }

    }

    private fun LoginButtonClick(){
        btnLoginSubmit.setOnClickListener {
            val tempEmail    = etLoginEmail.text.toString().trim()
            val tempPassword = etLoginPassword.text.toString().trim()
            if (tempEmail == ""){
                etLoginEmail.requestFocus()
                etLoginEmail.error = "E-mail address is required"
                return@setOnClickListener
            }
            if (!Utils.isValidEmail(tempEmail)){
                etLoginEmail.requestFocus()
                etLoginEmail.error = "Enter a valid e-mail address"
                return@setOnClickListener
            }
            if (tempPassword == ""){
                etLoginPassword.requestFocus()
                etLoginPassword.error = "Password is required"
                return@setOnClickListener
            }
            submitClick(tempEmail,tempPassword)
        }
    }

    private fun ForgotPassword(){
        tvLoginForgot.setOnClickListener {
            startActivity(Intent(this@LoginActivity,ForgotPasswordActivity::class.java))
        }
    }

    private fun RegisterNow(){
        tvLoginRegisterNow.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        btnLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    @Suppress("DEPRECATION")
    private fun twitterLogin(){
        //twitter login start
        btnLoginTwitter?.callback = object : com.twitter.sdk.android.core.Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                val session = TwitterCore.getInstance().sessionManager.activeSession
                val userId = session.userId.toString()
                TwitterCore.getInstance().apiClient.accountService.verifyCredentials(true, true, true).enqueue(object: com.twitter.sdk.android.core.Callback<User>() {
                    override fun success(result: Result<User>?) {
                        val user = result?.data
                        val profileImage = user?.profileImageUrl
                        val email = user?.email
                        val name = user?.name
                        socialLogin(name!!,email!!,profileImage!!,userId,"twitter")
                        android.webkit.CookieManager.getInstance().removeAllCookie()
                        logoutTwitter()
                    }

                    override fun failure(exception: TwitterException?) {
                        Log.d("aaaaaaaaaaaa","Email failed")
                    }
                })
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(this@LoginActivity, "Authentication failed! ${exception?.message}", Toast.LENGTH_LONG).show()
            }
        }

        ivLoginTwitter.setOnClickListener {
            btnLoginTwitter.performClick()
        }
        //twitter end
    }

    private fun googleLogin(){
        //google login start
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleApiClient = GoogleSignIn.getClient(this, gso)
        google_signIn.setOnClickListener {
            val signInIntent = mGoogleApiClient?.signInIntent
            startActivityForResult(signInIntent, 7)
        }
        //google end
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }
            if (allgranted) { }
            else if (ActivityCompat.shouldShowRequestPermissionRationale(this@LoginActivity, permissionsRequired[0])) {
                ActivityCompat.requestPermissions(this@LoginActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
        }
    }

    private fun submitClick(tempEmail: String, tempPassword: String) {
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().login(tempEmail,tempPassword)
        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>?, response: Response<LoginModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        if (cbLoginRememberMe.isChecked){
                            Utils.setUserLogin(true,data.id,data.name,data.email,data.reffrel,data.phone,data.photo,data.sess!!,data.gender)
                        }
                        else{
                            Utils.setUserLogin(false,data.id,data.name,data.email,data.reffrel,data.phone,data.photo,data.sess!!,data.gender)
                        }
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finish()
                    } else {
                        Utils.showToast(this@LoginActivity,data?.msg!!)
                    }
                } else {
                    Utils.showToast(this@LoginActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<LoginModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@LoginActivity,resources.getString(R.string.api_failure))
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
        else{
            btnLoginTwitter.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>?) {
        try {
            val account = completedTask?.getResult(ApiException::class.java)
            val personName = account?.displayName
            val email = account?.email
            val session = account?.id
            val photoUrl = account?.photoUrl.toString()
            socialLogin(personName!!,email!!,photoUrl,session!!,"google")
            logoutGoogle()
        } catch (e: ApiException) {
            Utils.showToast(this,"signInResult:failed code=" + e.statusCode)
        }
    }

    private fun logoutTwitter() {
        TwitterCore.getInstance().sessionManager.clearActiveSession()
    }

    private fun logoutGoogle() {
        mGoogleApiClient?.signOut()
    }

    private fun socialLogin(name:String,email:String,photo:String,uniqueId:String,type:String){
        Utils.showLoadingDialog(this)
        val call = ApiClient().getClient().socialLogin(name,email,photo,uniqueId,type,"SO")
        call.enqueue(object : Callback<LoginModel> {
            override fun onResponse(call: Call<LoginModel>?, response: Response<LoginModel>?) {
                Utils.cancelLoading()
                if (response!!.body() != null && response.isSuccessful) {
                    val data = response.body()?.response?.get(0)
                    if (data?.status == "SUCCESS") {
                        Utils.setUserLogin(true,data.id,data.name,data.email,data.reffrel,data.phone,data.photo,data.sess!!,data.gender)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                        finish()
                    } else {
                        Utils.showToast(this@LoginActivity,data?.msg!!)
                    }
                } else {
                    Utils.showToast(this@LoginActivity,resources.getString(R.string.no_response))
                }
            }
            override fun onFailure(call: Call<LoginModel>?, t: Throwable?) {
                Utils.cancelLoading()
                Utils.showToast(this@LoginActivity,resources.getString(R.string.api_failure))
            }
        })
    }
}
