package com.triviallanguages.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import com.triviallanguages.data.repositories.UserRepository
import com.triviallanguages.util.Coroutines

class HomeViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var homeListener: HomeListener? = null

    fun getUserLevel() {
        Coroutines.main {
            homeListener?.onLevelGet(repository.getUserLevel()?.toInt()!!)
        }
    }

    fun onPreviousButtonClick(view: View) {
        homeListener?.onPreviousClick()
    }

    fun onNextButtonClick(view: View) {
        homeListener?.onNextClick()
    }

    fun onPlayButtonClick(view: View) {
        homeListener?.onPlayClick()
    }
}