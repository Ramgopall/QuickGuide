package com.triviallanguages.ui.login

import android.content.Intent
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.triviallanguages.Constant.Companion.GOOGLE_SIGN_IN
import com.triviallanguages.data.firebase.FirebaseSource
import com.triviallanguages.data.repositories.UserRepository
import com.triviallanguages.util.ActivityNavigation
import com.triviallanguages.util.Coroutines
import com.triviallanguages.util.LiveMessageEvent
import com.triviallanguages.util.NoInternetException

@Suppress("UNUSED_PARAMETER")
class LoginViewModel(
    private val fireBaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModel() {

    var loginListener: LoginListener? = null
    val startActivityForResultEvent = LiveMessageEvent<ActivityNavigation>()

   // var imagePath = MutableLiveData<String>()

    fun getLoggedInUser() = repository.getUser()


    fun onLoginButtonClick(view: View){
        val signInIntent = fireBaseSource.googleSignInClient.signInIntent
        startActivityForResultEvent.sendEvent { startActivityForResult(signInIntent, GOOGLE_SIGN_IN) }
    }

    fun onResultFromActivity(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            GOOGLE_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                googleSignInComplete(task)
            }
        }
    }

    private fun googleSignInComplete(completedTask: Task<GoogleSignInAccount>) {
        try {
            loginListener?.onStarted()
            val account = completedTask.getResult(ApiException::class.java)
            val personName = account?.displayName
            val email = account?.email
            val photoUrl = account?.photoUrl.toString()

            Coroutines.main {
                try {
                    val authResponse = repository.userLogin(email.toString(),personName.toString(),photoUrl)
                    authResponse.users?.let {
                        loginListener?.onSuccess()
                        repository.saveUser(it)
                        return@main
                    }
                    loginListener?.onFailure(authResponse.message!!)
                }catch(e: ApiException){
                    loginListener?.onFailure(e.message!!)
                }catch (e: NoInternetException){
                    loginListener?.onFailure(e.message!!)
                }
            }
            fireBaseSource.googleSignInClient.signOut()
        } catch (e: ApiException) {
            e.message?.let { loginListener?.onFailure(it) }
        }
    }
}