package com.example.reporter_news_app.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe Image Changes
        viewModel.imageIndex.observe(viewLifecycleOwner) { index ->
            binding.imageView.setImageResource(viewModel.getImage(index))

            // Update button text
            binding.nextButton.text = if (viewModel.isLastImage()) "Get Started" else "Next"

            // Enable/Disable Back Button
            binding.backButton.isEnabled = !viewModel.isFirstImage()

            // Update Dots Indicator
            updateDots(index)
        }

        // Handle Next Button Click
        binding.nextButton.setOnClickListener {
            if (viewModel.isLastImage()) {
                findNavController().navigate(R.id.nav_login)
            } else {
                viewModel.nextImage()
            }
        }

        // Handle Back Button Click
        binding.backButton.setOnClickListener {
            viewModel.prevImage()
        }

        setupDots(viewModel.getImageCount()) // Initialize dots
    }

    private fun setupDots(count: Int) {
        binding.dotsLayout.removeAllViews() // Clear existing dots

        for (i in 0 until count) {
            val dot = ImageView(requireContext()).apply {
                setImageResource(R.drawable.dot_unselected) // Default state
                layoutParams = LinearLayout.LayoutParams(50, 50).apply {
                    marginEnd = 8
                }
            }
            binding.dotsLayout.addView(dot)
        }
    }

    private fun updateDots(currentIndex: Int) {
        for (i in 0 until binding.dotsLayout.childCount) {
            val dot = binding.dotsLayout.getChildAt(i) as ImageView
            dot.setImageResource(
                if (i == currentIndex) R.drawable.dot_selected else R.drawable.dot_unselected
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
