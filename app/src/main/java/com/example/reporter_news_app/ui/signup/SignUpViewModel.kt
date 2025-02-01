package com.example.reporter_news_app.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    private val _signUpState = MutableLiveData<Boolean>()
    val signUpState: LiveData<Boolean> get() = _signUpState

    fun signUp(username: String, email: String, password: String) {
        // Simulate a sign-up process (Replace this with API call)
        _signUpState.value = username.isNotEmpty() && email.contains("@") && password.length >= 6
    }
}
