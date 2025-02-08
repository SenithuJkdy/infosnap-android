package com.example.reporter_news_app.ui.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method

class LoginViewModel : ViewModel() {
    // Create separate states for login result and role
    private val _loginState = MutableLiveData<LoginResult>()
    val loginState: LiveData<LoginResult> get() = _loginState

    fun login(context: Context, email: String, password: String) {
        val url = "http://192.168.1.100:5001/api/auth/login"

        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            requestBody,
            { response ->
                try {
                    // Extract all necessary data from response
                    val token = response.getString("token")
                    val role = response.getString("role")
                    val authorId = response.getString("authorId")

                    // Store data in SharedPreferences
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("auth_token", token)
                        putString("user_role", role)
                        putString("author_id", authorId)
                        apply()
                    }

                    // Update login state based on role
                    _loginState.value = LoginResult(
                        isSuccess = true,
                        role = role
                    )

                    // Log the stored data (for debugging)
                    Log.d("LoginViewModel", "Login successful - Role: $role, AuthorId: $authorId")

                } catch (e: JSONException) {
                    Log.e("LoginViewModel", "JSON parsing error: ${e.message}")
                    _loginState.value = LoginResult(
                        isSuccess = false,
                        errorMessage = "Failed to parse login response"
                    )
                }
            },
            { error ->
                Log.e("LoginViewModel", "Network error: ${error.message}")
                _loginState.value = LoginResult(
                    isSuccess = false,
                    errorMessage = error.message ?: "Network error occurred"
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}

