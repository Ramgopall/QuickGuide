package com.duke.visitormanagement.ui.newVisitor

interface NewVisitorListener {
    fun onStarted()
    fun onBackClick()
    fun onSuccess(catList: ArrayList<String>)
    fun onSubmitSuccess(name: String,number:String)
    fun onFailure(message: String)
    fun onImageClick()
    fun onDialogCrossClick()
    fun onSpinner1Change(subCatList: ArrayList<String>)
    fun onSpinner2Change(clientUserList: ArrayList<String>)
}