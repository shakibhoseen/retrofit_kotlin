package com.example.rectrofitandroid.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rectrofitandroid.api.QuoteService
import com.example.rectrofitandroid.models.QuoteList

class QuoteRepository(private val quoteService: QuoteService) {
    private val quotesLiveData = MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList>
    get() = quotesLiveData

    suspend fun getQuotes(page: Int){
        val result = quoteService.getQuotes(page)

        if(result?.body() !=null){
            quotesLiveData.postValue(result.body())
        }
    }
}