package com.example.rectrofitandroid

import android.app.Application
import com.example.rectrofitandroid.api.QuoteService
import com.example.rectrofitandroid.api.RetrofitHelper
import com.example.rectrofitandroid.db.QuoteDatabase
import com.example.rectrofitandroid.repository.QuoteRepository

class QuoteApplication: Application() {
    lateinit var quoteRepository: QuoteRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RetrofitHelper.getInstance().create(QuoteService::class.java)
        val database = QuoteDatabase.getDatabase(applicationContext)
        quoteRepository = QuoteRepository(quoteService, database, applicationContext)
    }
}