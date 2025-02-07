package com.example.reporter_news_app.ui.login

import android.content.Context
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
    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> get() = _loginState

    fun login(context: Context, email: String, password: String) {
        val url = "http://192.168.1.100:5001/api/auth/login"

        val requestBody = JSONObject()
        requestBody.put("email", email)
        requestBody.put("password", password)

        val request = object : JsonObjectRequest(Method.POST, url, requestBody,
            Response.Listener { response ->
                try {
                    val token = response.getString("token")

                    // Store token (SharedPreferences)
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("auth_token", token)
                        apply()
                    }

                    _loginState.value = true
                } catch (e: JSONException) {
                    _loginState.value = false
                }
            },
            Response.ErrorListener {
                _loginState.value = false
            }) {

            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

}

