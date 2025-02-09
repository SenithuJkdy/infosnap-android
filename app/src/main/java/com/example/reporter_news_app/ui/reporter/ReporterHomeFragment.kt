package com.example.reporter_news_app.ui.reporter

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentReporterHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ReporterHomeFragment : Fragment() {
    private lateinit var binding: FragmentReporterHomeBinding
    private val viewModel: ReporterHomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReporterHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubmissionForm()
        observeSubmissionState()
    }

    private fun setupSubmissionForm() {
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            when {
                title.isBlank() -> {
                    binding.titleEditText.error = "Title is required"
                }
                content.isBlank() -> {
                    binding.contentEditText.error = "Content is required"
                }
                else -> {
                    // Show loading state
                    binding.progressBar.isVisible = true
                    binding.submitButton.isEnabled = false

                    viewModel.submitArticle(requireContext(), title, content)
                }
            }
        }
    }

    private fun observeSubmissionState() {
        viewModel.submissionState.observe(viewLifecycleOwner) { result ->
            // Hide loading state
            binding.progressBar.isVisible = false
            binding.submitButton.isEnabled = true

            if (result.isSuccess) {
                hideKeyboard()
                // Clear form
                binding.titleEditText.text?.clear()
                binding.contentEditText.text?.clear()

                showSuccessDialog()
            } else {
                showErrorMessage(result.message)
            }
        }
    }

    private fun showSuccessDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Success")
            .setMessage("Your article has been submitted successfully")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showErrorMessage(message: String?) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage(message ?: "Failed to submit article")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    // hide keyboard after signup
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}