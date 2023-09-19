package com.example.rectrofitandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.rectrofitandroid.repository.Response
import com.example.rectrofitandroid.view_model.MainViewModel
import com.example.rectrofitandroid.view_model.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tag = "MainActivity3"
         progressBar = findViewById(R.id.progressBar)

        val repository = (application as QuoteApplication).quoteRepository

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]

        mainViewModel.quotes.observe(this) {

            Log.d(tag, "onCreate: $it")
            when(it){
                is Response.Loading ->{
                    progressBar.visibility = View.VISIBLE
                }
                is Response.Error ->{
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                }is Response.Success ->{
                progressBar.visibility = View.GONE

                    if(it.data!=null){
                        Toast.makeText(this, it.data.results.size.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }

        }


    }
}