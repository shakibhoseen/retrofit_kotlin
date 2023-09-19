package com.example.rectrofitandroid.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rectrofitandroid.R
import com.example.rectrofitandroid.databinding.AuthorItemBinding
import com.example.rectrofitandroid.databinding.ProgressItemBinding
import com.example.rectrofitandroid.models.Result as ResultContent


class AuthorAdapter(private val onNoteClicked: (ResultContent) -> Unit) :
    ListAdapter<ResultContent, RecyclerView.ViewHolder>(ComparatorDiffUtil()) {
    private val VIEW_TYPE_DATA = 0
    private val VIEW_TYPE_PROGRESS = 1
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        when (viewType) {
            VIEW_TYPE_DATA -> {
                val binding =
                    AuthorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return AuthorViewHolder(binding)
            }
            VIEW_TYPE_PROGRESS -> {
                val binding =
                    ProgressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ProgressViewHolder(binding)
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is AuthorViewHolder) {
            val note = getItem(position)
            note?.let {
                holder.bind(it)
            }
        } else if (holder is ProgressViewHolder) {
            // Bind the progress item as needed (e.g., start animation)
            holder.bind(position)
        }
    }

    override fun getItemCount(): Int {
        val count = super.getItemCount()
        return if(isLoading)  count+1 else count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isLoading) {
            VIEW_TYPE_PROGRESS
        } else {
            VIEW_TYPE_DATA
        }
    }

    inner class AuthorViewHolder(private val binding: AuthorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quote: ResultContent) {
            binding.title.text = quote.author
            binding.desc.text = quote.content
            binding.root.setOnClickListener {
                onNoteClicked(quote)
            }
        }

    }

    inner class ProgressViewHolder(private val binding: ProgressItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // You can customize the progress item's appearance and behavior here
            fun bind(id: Int){
                binding.progressBar.visibility = View.VISIBLE
            }
    }

    class ComparatorDiffUtil : DiffUtil.ItemCallback<ResultContent>() {
        override fun areItemsTheSame(oldItem: ResultContent, newItem: ResultContent): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ResultContent, newItem: ResultContent): Boolean {
            return oldItem == newItem
        }
    }


    fun showLoading() {
        isLoading = true
        notifyItemInserted(itemCount+1)
    }

    fun hideLoading() {
        isLoading = false
        notifyItemRemoved(itemCount)
    }
}