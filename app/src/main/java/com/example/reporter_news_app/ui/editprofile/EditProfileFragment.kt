package com.example.reporter_news_app.ui.editprofile

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reporter_news_app.R
import com.example.reporter_news_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private lateinit var viewModel: EditProfileViewModel
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private var profileImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Fill your Profile"  // Set custom title
        toolbar.visibility = View.VISIBLE // Ensure the toolbar is shown

        viewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        binding.profileImageView.setOnClickListener { openGallery() }

        binding.nextButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val fullName = binding.fullNameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val phone = binding.phoneNoEditText.text.toString()

            viewModel.saveProfile(username, fullName, email, phone, profileImageUri)
        }

        viewModel.profileCompleted.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile Completed!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.nav_home) // Redirect to Home
            } else {
                Toast.makeText(requireContext(), "Please fill all fields correctly.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            profileImageUri = data?.data
            binding.profileImageView.setImageURI(profileImageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}