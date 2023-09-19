package com.example.rectrofitandroid.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rectrofitandroid.models.QuoteList
import com.example.rectrofitandroid.repository.QuoteRepository
import com.example.rectrofitandroid.repository.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: QuoteRepository) : ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getQuotes(1)
        }
    }
    val quotes : LiveData<Response<QuoteList>>
    get() = repository.quotes
}