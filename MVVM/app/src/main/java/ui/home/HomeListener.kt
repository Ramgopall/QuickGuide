package com.triviallanguages.ui.home

interface HomeListener {
    fun onNextClick()
    fun onPreviousClick()
    fun onPlayClick()
    fun onLevelGet(level:Int)
}