package com.example.reporter_news_app.ui.news

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.reporter_news_app.ui.Constants
import org.json.JSONException

class NewsViewModel : ViewModel() {
    private val _newsState = MutableLiveData<List<NewsResponse>>()
    val newsState: LiveData<List<NewsResponse>> get() = _newsState

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    fun fetchApprovedNews(context: Context) {
        val url = "${Constants.BASE_URL}/api/articles/all"

        val request = object : JsonArrayRequest(
            Method.GET,
            url,
            null,
            { response ->
                // Log the entire response
                Log.d("NewsAPI", "Raw Response: $response")

                try {
                    val newsList = mutableListOf<NewsResponse>()
                    for (i in 0 until response.length()) {
                        val newsJson = response.getJSONObject(i)
                        // Log each news object
                        Log.d("NewsAPI", "News Object $i: $newsJson")

                        try {
                            if (newsJson.getString("status") == "approved") {
                                val authorJson = newsJson.getJSONObject("authorId")
                                // Log author data
                                Log.d("NewsAPI", "Author Object: $authorJson")

                                val author = Author(
                                    _id = authorJson.getString("_id"),
                                    name = authorJson.getString("name"),
                                    email = authorJson.getString("email")
                                )

                                val news = NewsResponse(
                                    _id = newsJson.getString("_id"),
                                    title = newsJson.getString("title"),
                                    content = newsJson.getString("content"),
                                    authorId = author,
                                    status = newsJson.getString("status"),
                                    images = emptyList(),
                                    createdAt = newsJson.getString("createdAt"),
                                    __v = newsJson.getInt("__v")
                                )
                                newsList.add(news)
                                // Log successful parsing
                                Log.d("NewsAPI", "Successfully parsed news item: ${news.title}")
                            }
                        } catch (e: JSONException) {
                            // Log specific parsing errors for each object
                            Log.e("NewsAPI", "Error parsing news object $i: ${e.message}")
                            Log.e("NewsAPI", "Problematic JSON: $newsJson")
                        }
                    }
                    _newsState.value = newsList
                    Log.d("NewsAPI", "Total news items parsed: ${newsList.size}")

                } catch (e: JSONException) {
                    // Log the main parsing error
                    Log.e("NewsAPI", "Main parsing error: ${e.message}")
                    Log.e("NewsAPI", "Stack trace:", e)
                    _errorState.value = "Failed to parse news data: ${e.message}"
                }
            },
            { error ->
                // Log network errors
                Log.e("NewsAPI", "Network error: ${error.message}")
                if (error.networkResponse != null) {
                    try {
                        val errorStr = String(error.networkResponse.data, Charsets.UTF_8)
                        Log.e("NewsAPI", "Error response: $errorStr")
                    } catch (e: Exception) {
                        Log.e("NewsAPI", "Error parsing error response", e)
                    }
                }
                _errorState.value = "Failed to fetch news: ${error.message}"
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                return hashMapOf("Content-Type" to "application/json")
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}