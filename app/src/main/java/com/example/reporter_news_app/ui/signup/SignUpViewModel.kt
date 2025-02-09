package com.example.reporter_news_app.ui.signup

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.reporter_news_app.ui.Constants
import org.json.JSONException
import org.json.JSONObject

class SignUpViewModel : ViewModel() {

    private val _signUpState = MutableLiveData<Boolean>()
    val signUpState: LiveData<Boolean> get() = _signUpState

    fun signUp(context: Context, name: String, email: String, password: String, role: String) {
        val url = "${Constants.BASE_URL}/api/auth/register" // Update with actual endpoint

        val requestBody = JSONObject().apply {
            put("name", name)
            put("email", email)
            put("password", password)
            put("role", role)
        }

        val request = object : JsonObjectRequest(Method.POST, url, requestBody,
            Response.Listener { response ->
                try {
                    // ✅ Check if "message" exists
                    _signUpState.value = response.has("message")
                } catch (e: JSONException) {
                    _signUpState.value = false
                }
            },
            Response.ErrorListener { error ->
                try {
                    val errorMsg = String(error.networkResponse.data, Charsets.UTF_8)
                    val errorJson = JSONObject(errorMsg)
                    if (errorJson.has("error")) {
                        Log.e("SignUpError", errorJson.getString("error"))
                    }
                } catch (e: Exception) {
                    Log.e("SignUpError", "Unknown error")
                }
                _signUpState.value = false
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
