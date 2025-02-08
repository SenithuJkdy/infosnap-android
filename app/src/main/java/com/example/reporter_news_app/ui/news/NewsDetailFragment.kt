package com.example.reporter_news_app.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentNewsDetailBinding

class NewsDetailFragment : Fragment() {
    private lateinit var binding: FragmentNewsDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get news data from arguments
        arguments?.let { args ->
            val newsId = args.getString("newsId") ?: return
            val title = args.getString("title") ?: ""
            val content = args.getString("content") ?: ""
            val authorName = args.getString("authorName") ?: ""
            val createdAt = args.getString("createdAt") ?: ""

            displayNewsDetails(title, content, authorName, createdAt)
        }
    }

    private fun displayNewsDetails(title: String, content: String, authorName: String, createdAt: String) {
        binding.apply {
            titleTextView.text = title
            contentTextView.text = content
            authorTextView.text = "By $authorName"
            dateTextView.text = createdAt // You might want to format this
        }
    }
}