package com.triviallanguages.ui.question


interface QuestionListener {
    fun onStarted()
    fun onSuccess(message: String?)
    fun onOptionSelect(answer: String,optionButtonNo:String)
    fun onSpickerClick()
    fun onHelpClick()
    fun onFailure(message: String)
    fun onGetCounts(getCountIds: Int)
}