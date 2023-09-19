package com.example.rectrofitandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.rectrofitandroid.databinding.ActivityMainBinding
import com.example.rectrofitandroid.repository.Response
import com.example.rectrofitandroid.view_model.MainViewModel
import com.example.rectrofitandroid.view_model.MainViewModelFactory
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.rectrofitandroid.ui.AuthorAdapter
import com.example.rectrofitandroid.models.Result as ResultContent


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AuthorAdapter
    private val tag = "MainActivity3"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)  // viewBinding

        adapter = AuthorAdapter(::onAuthorClicked)

        binding.authorList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.authorList.adapter = adapter

        // Implement a scroll listener to detect when the user scrolls to the end
        binding.authorList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val manger = (binding.authorList.layoutManager as StaggeredGridLayoutManager);
                val visibleItemCount = manger.childCount
                val totalItemCount = manger.itemCount
                val firstVisibleItemPosition = manger.findFirstVisibleItemPositions(null)

                val isNearEnd = firstVisibleItemPosition.any {
                    it >= totalItemCount - visibleItemCount
                }
                Log.d(
                    tag,
                    "onScrolled: visibleItemCount $visibleItemCount - firstVisibleItemPosition $firstVisibleItemPosition totalItemCount -$totalItemCount"
                )
                Log.d(tag, "onScrolled: $isNearEnd")
                if (/*!isLoading && !isLastPage &&*/ isNearEnd) {
//                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
//                        && firstVisibleItemPosition >= 0
//                    ) {
//                        loadMoreData()
//                    }
                }
            }
        })

        observerData()
        var isSwitch: Boolean = false
        binding.textView.setOnClickListener {
            if (!isSwitch) {
                adapter.showLoading()
                isSwitch = !isSwitch
            } else {
                adapter.hideLoading()
                isSwitch = !isSwitch
            }
        }

    }


    fun observerData() {
        val repository = (application as QuoteApplication).quoteRepository
        mainViewModel =
            ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]



        mainViewModel.quotes.observe(this) {

            Log.d(tag, "onCreate: $it")
            when (it) {
                is Response.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Response.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
                }
                is Response.Success -> {
                    binding.progressBar.visibility = View.GONE


                    if (it.data != null) {
                        adapter.submitList(it.data.results)
                        Toast.makeText(this, it.data.results.size.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

        }
    }


    private fun onAuthorClicked(result: ResultContent) {
        Toast.makeText(this, result.author, Toast.LENGTH_LONG).show()
    }


}