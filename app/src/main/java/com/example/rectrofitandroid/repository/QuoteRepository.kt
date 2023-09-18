package com.example.rectrofitandroid.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rectrofitandroid.api.QuoteService
import com.example.rectrofitandroid.db.QuoteDatabase
import com.example.rectrofitandroid.models.QuoteList
import com.example.rectrofitandroid.utils.NetworkUtils

class QuoteRepository(
    private val quoteService: QuoteService,
    private val quoteDatabase: QuoteDatabase,
    private val applicationContext: Context
) {
    private val quotesLiveData = MutableLiveData<QuoteList>()

    val quotes: LiveData<QuoteList>
    get() = quotesLiveData

    suspend fun getQuotes(page: Int){
        if(!NetworkUtils.isInternetAvailable(applicationContext)){
            //ToDO offline code and execute
            val quotesM = quoteDatabase.quoteDao().getQuotes()
            val quotesList = QuoteList(1,1,1,quotesM, 2, 2)
            quotesLiveData.postValue(quotesList)
            return;
        }
        val result = quoteService.getQuotes(page)

        if(result?.body() !=null){
            quoteDatabase.quoteDao().addQuotes(result.body()!!.results)
            quotesLiveData.postValue(result.body())
        }
    }
}