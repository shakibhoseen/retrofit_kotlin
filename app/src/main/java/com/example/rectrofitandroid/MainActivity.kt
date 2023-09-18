package com.example.rectrofitandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.rectrofitandroid.api.QuoteService
import com.example.rectrofitandroid.api.RetrofitHelper
import com.example.rectrofitandroid.repository.QuoteRepository
import com.example.rectrofitandroid.view_model.MainViewModel
import com.example.rectrofitandroid.view_model.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val  TAG = "MainActivity3"

        val repository = (application as QuoteApplication).quoteRepository

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.quotes.observe(this) {
            Log.d(TAG, "onCreate: " + it.results.toString())
            Toast.makeText(this, it.results.size.toString(), Toast.LENGTH_LONG).show()
        }


    }
}