package com.example.reporter_news_app.ui.reporter

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ReporterHomeViewModel : ViewModel() {
    private val _submissionState = MutableLiveData<SubmissionResult>()
    val submissionState: LiveData<SubmissionResult> get() = _submissionState

    data class SubmissionResult(
        val isSuccess: Boolean,
        val message: String? = null
    )

    fun submitArticle(context: Context, title: String, content: String) {
        val url = "http://192.168.1.100:5001/api/articles/create"

        // Get authorId from SharedPreferences
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val authorId = sharedPref.getString("author_id", null)

        if (authorId == null) {
            _submissionState.value = SubmissionResult(
                isSuccess = false,
                message = "Author ID not found. Please login again."
            )
            return
        }

        val requestBody = JSONObject().apply {
            put("title", title)
            put("content", content)
            put("authorId", authorId)
        }

        // Log the request for debugging
        Log.d("ReporterHome", "Submitting article: $requestBody")

        val request = object : JsonObjectRequest(
            Method.POST,
            url,
            requestBody,
            { response ->
                Log.d("ReporterHome", "Success response: $response")
                _submissionState.value = SubmissionResult(
                    isSuccess = true,
                    message = "Article submitted successfully"
                )
            },
            { error ->
                Log.e("ReporterHome", "Error submitting article: ${error.message}")
                val errorMessage = try {
                    String(error.networkResponse.data, Charsets.UTF_8)
                } catch (e: Exception) {
                    error.message ?: "Unknown error occurred"
                }
                _submissionState.value = SubmissionResult(
                    isSuccess = false,
                    message = errorMessage
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                // Add token if your API requires authentication
                sharedPref.getString("auth_token", null)?.let {
                    headers["Authorization"] = "Bearer $it"
                }
                return headers
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}