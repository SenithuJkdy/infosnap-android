package com.example.reporter_news_app.ui.signup

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
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var viewModel: SignUpViewModel
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        binding.signUpButton.setOnClickListener {
            val name = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            var role   = binding.userRoleSpinner.selectedItem.toString().trim()

            // Spinner default value validation handle and other fields
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == ("Please Select Who Are You")) {
                Toast.makeText(requireContext(), "Please check the all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            role = role.lowercase()


            viewModel.signUp(requireContext(), name, email, password,role)
        }

        // For "don't have an account ? Sign Up"
        val fullText = "Already have an account ? Login"
        val spannableString = SpannableString(fullText)
        val loginUpStart = fullText.indexOf("Login")
        val loginUpEnd = loginUpStart + "Login".length
        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.sky_blue)),
            loginUpStart, loginUpEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.signUpTextView.text = spannableString // Set to your signUpTextView

        binding.signUpTextView.setOnClickListener{
            findNavController().navigate(R.id.nav_login) // Navigate to Login when hyperlink clicked
        }

        viewModel.signUpState.observe(viewLifecycleOwner) { success ->
            // role based navigation
            var role   = binding.userRoleSpinner.selectedItem.toString().trim()
            role = role.lowercase()

            if (success) {
                Toast.makeText(requireContext(), "Sign-up successful! Please log in.", Toast.LENGTH_SHORT).show()
                if ( role =="user"){
                    findNavController().navigate(R.id.nav_news) // Navigate to login
                }
                else if (role == "reporter"){
                    findNavController().navigate(R.id.nav_reporter_home) // Navigate to login
                }
            } else {
                Toast.makeText(requireContext(), "Sign-up failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
