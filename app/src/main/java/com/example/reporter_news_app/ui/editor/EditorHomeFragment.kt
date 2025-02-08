package com.example.reporter_news_app.ui.editor

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentEditorHomeBinding

class EditorHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditorHomeBinding.inflate(inflater, container, false)

        binding.reviewNewsButton.setOnClickListener {
            findNavController().navigate(R.id.nav_home) // Navigate to review page
        }

        return binding.root
    }
}