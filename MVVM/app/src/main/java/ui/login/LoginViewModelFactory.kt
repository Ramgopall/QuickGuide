package com.triviallanguages.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.triviallanguages.data.firebase.FirebaseSource
import com.triviallanguages.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class LoginViewModelFactory(
    private val fireBaseSource: FirebaseSource,
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(fireBaseSource,repository) as T
    }
}