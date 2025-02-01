package com.example.reporter_news_app.ui.landing

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentLandingBinding

class LandingFragment : Fragment() {

    private lateinit var binding: FragmentLandingBinding
    private val viewModel: LandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)

        // Observe Image Changes
        viewModel.imageIndex.observe(viewLifecycleOwner) {
            binding.imageView.setImageResource(viewModel.getImage())

            // Update button text dynamically
            if (viewModel.isLastImage()) {
                binding.nextButton.text = "Get Started"

            } else {
                binding.nextButton.text = "Next"
            }

            // Redirect after Get Started button clicked
            if (binding.nextButton.text == "Get Started"){
                binding.nextButton.setOnClickListener {
                    findNavController().navigate(R.id.nav_gallery) // Redirect to HomeFragment
                }
            }


            // Enable/Disable Back Button
            binding.backButton.isEnabled = !viewModel.isFirstImage()
        }

        // Handle Next Button Click
        binding.nextButton.setOnClickListener {
            if (viewModel.isLastImage()) {
                // Perform an action (e.g., navigate to another screen)
            } else {
                viewModel.nextImage()
            }
        }

        // Handle Back Button Click
        binding.backButton.setOnClickListener {
            viewModel.prevImage()
        }

        return binding.root

    }

}
