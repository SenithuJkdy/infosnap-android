package com.example.reporter_news_app.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reporter_news_app.databinding.FragmentEditorHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditorHomeFragment : Fragment() {
    private lateinit var binding: FragmentEditorHomeBinding
    private val viewModel: EditorHomeViewModel by viewModels()
    private lateinit var articlesAdapter: EditorArticlesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditorHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()

        viewModel.fetchSubmittedArticles(requireContext())
    }

    private fun setupRecyclerView() {
        articlesAdapter = EditorArticlesAdapter(
            onApprove = { article ->
                showConfirmationDialog(article._id, "approved")
            },
            onReject = { article ->
                showConfirmationDialog(article._id, "rejected")
            }
        )

        binding.recyclerView.apply {
            adapter = articlesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupObservers() {
        viewModel.articlesState.observe(viewLifecycleOwner) { articles ->
            binding.progressBar.isVisible = false
            binding.emptyView.isVisible = articles.isEmpty()
            binding.recyclerView.isVisible = articles.isNotEmpty()
            articlesAdapter.submitList(articles)
        }

        viewModel.updateStatus.observe(viewLifecycleOwner) { result ->
            Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showConfirmationDialog(articleId: String, newStatus: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirm Action")
            .setMessage("Are you sure you want to mark this article as $newStatus?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.updateArticleStatus(requireContext(), articleId, newStatus)
            }
            .setNegativeButton("No", null)
            .show()
    }
}