package com.duke.visitormanagement.ui.newVisitor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duke.visitormanagement.data.repositories.NewVisitorRepository

@Suppress("UNCHECKED_CAST")
class NewVisitorViewModelFactory(
    private val repository: NewVisitorRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewVisitorViewModel(repository) as T
    }
}