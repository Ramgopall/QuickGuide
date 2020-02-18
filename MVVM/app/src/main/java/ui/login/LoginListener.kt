package com.triviallanguages.ui.login


interface LoginListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}