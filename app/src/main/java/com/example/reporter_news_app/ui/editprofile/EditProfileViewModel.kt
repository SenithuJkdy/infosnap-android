package com.example.reporter_news_app.ui.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileViewModel : ViewModel() {

    private val _profileCompleted = MutableLiveData<Boolean>()
    val profileCompleted: LiveData<Boolean> get() = _profileCompleted

    fun saveProfile(username: String, fullName: String, email: String, phone: String, profileImage: Uri?) {
        // Assume success (replace with real database logic)
        _profileCompleted.value = username.isNotEmpty() && fullName.isNotEmpty() && email.contains("@") && phone.length >= 10
    }

}