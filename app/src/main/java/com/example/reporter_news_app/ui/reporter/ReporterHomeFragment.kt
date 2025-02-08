package com.example.reporter_news_app.ui.reporter

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentReporterHomeBinding

class ReporterHomeFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentReporterHomeBinding.inflate(inflater, container, false)

        binding.submitNewsButton.setOnClickListener {
            findNavController().navigate(R.id.nav_home) // Navigate to news submission page
        }

        return binding.root
    }
}