package com.example.reporter_news_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> get() = _loginState

    fun login(username: String, password: String) {
        // Simulate a login process (Replace with API call)
        _loginState.value = username == "admin" && password == "admin"
    }
}
