package com.example.reporter_news_app.ui.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reporter_news_app.databinding.ItemEditorArticleBinding
import com.example.reporter_news_app.ui.news.NewsResponse

class EditorArticlesAdapter(
    private val onApprove: (NewsResponse) -> Unit,
    private val onReject: (NewsResponse) -> Unit
) : ListAdapter<NewsResponse, EditorArticlesAdapter.ArticleViewHolder>(ArticleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemEditorArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding, onApprove, onReject)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ArticleViewHolder(
        private val binding: ItemEditorArticleBinding,
        private val onApprove: (NewsResponse) -> Unit,
        private val onReject: (NewsResponse) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsResponse) {
            binding.apply {
                titleTextView.text = article.title
                contentTextView.text = article.content
                authorTextView.text = "By: ${article.authorId.name}"
                dateTextView.text = formatDate(article.createdAt)

                approveButton.setOnClickListener { onApprove(article) }
                rejectButton.setOnClickListener { onReject(article) }
            }
        }

        private fun formatDate(dateString: String): String {
            // Add date formatting logic here
            return dateString
        }
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<NewsResponse>() {
        override fun areItemsTheSame(oldItem: NewsResponse, newItem: NewsResponse) =
            oldItem._id == newItem._id

        override fun areContentsTheSame(oldItem: NewsResponse, newItem: NewsResponse) =
            oldItem == newItem
    }
}