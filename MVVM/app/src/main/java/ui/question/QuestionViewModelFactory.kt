package com.triviallanguages.ui.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.triviallanguages.data.repositories.QuestionRepository

@Suppress("UNCHECKED_CAST")
class QuestionViewModelFactory(
    private val repository: QuestionRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionViewModel(repository) as T
    }
}