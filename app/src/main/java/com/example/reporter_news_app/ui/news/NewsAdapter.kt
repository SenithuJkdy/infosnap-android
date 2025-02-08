package com.example.reporter_news_app.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reporter_news_app.databinding.ItemNewsBinding

class NewsAdapter( private val onNewsClicked: (NewsResponse) -> Unit)
    : ListAdapter<NewsResponse, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding,onNewsClicked)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onNewsClicked: (NewsResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsResponse) {
            binding.apply {
                titleTextView.text = news.title
                contentTextView.text = news.content
                authorTextView.text = news.authorId.name
                dateTextView.text = formatDate(news.createdAt)

                // Set click listener on the entire item
                root.setOnClickListener {
                    onNewsClicked(news)
                }
            }
        }

        private fun formatDate(dateString: String): String {
            // Add your date formatting logic here
            return dateString
        }
    }

    class NewsDiffCallback : DiffUtil.ItemCallback<NewsResponse>() {
        override fun areItemsTheSame(oldItem: NewsResponse, newItem: NewsResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: NewsResponse, newItem: NewsResponse): Boolean {
            return oldItem == newItem
        }
    }
}