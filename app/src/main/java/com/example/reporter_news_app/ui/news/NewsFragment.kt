package com.example.reporter_news_app.ui.news

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentNewsBinding
import org.json.JSONException

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private val viewModel: NewsViewModel by viewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.fetchApprovedNews(requireContext())
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter { news ->
            // Navigate to detail fragment
            val bundle = Bundle().apply {
                putString("newsId", news._id)
                putString("title", news.title)
                putString("content", news.content)
                putString("authorName", news.authorId.name)
                putString("createdAt", news.createdAt)
            }
            findNavController().navigate(R.id.nav_news_detail, bundle)
        }
        binding.recyclerView.adapter = newsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupObservers() {
        viewModel.newsState.observe(viewLifecycleOwner) { newsList ->
            newsAdapter.submitList(newsList)
        }

        viewModel.errorState.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }
}