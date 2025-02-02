package com.example.reporter_news_app.ui.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(username, password)
        }

        // Start of SpannableString code
        // For "don't have an account ? Sign Up"
        val fullText = "don't have an account ? Sign Up"
        val spannableString = SpannableString(fullText)
        val signUpStart = fullText.indexOf("Sign Up")
        val signUpEnd = signUpStart + "Sign Up".length
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.sky_blue)),
            signUpStart, signUpEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.signUpTextView.text = spannableString // Set to your signUpTextView

        // For "Hello Again!"
        val againText = "Hello Again!"
        val spannableAgainString = SpannableString(againText)
        val againStart = againText.indexOf("Again!")
        val againEnd = againStart + "Again!".length
        spannableAgainString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.sky_blue)), // Use a different color
            againStart, againEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.heading.text = spannableAgainString // Assuming you have a TextView with this ID



        binding.signUpTextView.setOnClickListener {
            findNavController().navigate(R.id.nav_signup) // Navigate to Sign-Up when hyperlink clicked
        }

        viewModel.loginState.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.nav_home)
            } else {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
