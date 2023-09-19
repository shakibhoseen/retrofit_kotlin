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
    private val quotesLiveData = MutableLiveData<Response<QuoteList>>()

    val quotes: LiveData<Response<QuoteList>>
    get() = quotesLiveData

    suspend fun getQuotes(page: Int){
        if(!NetworkUtils.isInternetAvailable(applicationContext)){
            //ToDO offline code and execute
            val quotesM = quoteDatabase.quoteDao().getQuotes()
            val quotesList = QuoteList(1,1,1,quotesM, 2, 2)
            quotesLiveData.postValue(Response.Success(quotesList))
            return
        }

        try {
            quotesLiveData.postValue(Response.Loading())
            val result = quoteService.getQuotes(page)

            if(result.body() !=null){
                quoteDatabase.quoteDao().addQuotes(result.body()!!.results)
                quotesLiveData.postValue(Response.Success(result.body()))
            }
        }catch (e: Exception){
            quotesLiveData.postValue(Response.Error(e.message.toString()))
        }


    }
}