package com.example.reporter_news_app.ui.login

data class LoginResult(
    val isSuccess: Boolean,
    val role: String? = null,
    val errorMessage: String? = null
)
