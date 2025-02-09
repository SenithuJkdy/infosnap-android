package com.example.reporter_news_app.ui.login

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            viewModel.login(requireContext(), email, password)
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

        // Observe login state
        viewModel.loginState.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                hideKeyboard()
                when (result.role) {
                    "user" -> {
                        // Navigate to news feed
                        findNavController().navigate(R.id.nav_news)
                    }
                    "reporter" -> {
                        // Navigate to reporter dashboard (placeholder for now)
                        findNavController().navigate(R.id.nav_reporter_home)
                    }
                    "editor" -> {
                        // Navigate to editor dashboard (placeholder for now)
                        findNavController().navigate(R.id.nav_editor_home)
                    }
                    else -> {
                        // Handle unknown role
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Handle login failure
                Toast.makeText(
                    context,
                    result.errorMessage ?: "Login failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
    // hide keyboard after logged
    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
