package com.example.reporter_news_app.ui.editor

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.reporter_news_app.ui.Constants
import com.example.reporter_news_app.ui.news.Author
import com.example.reporter_news_app.ui.news.NewsResponse
import org.json.JSONException
import org.json.JSONObject

class EditorHomeViewModel : ViewModel() {
    private val _articlesState = MutableLiveData<List<NewsResponse>>()
    val articlesState: LiveData<List<NewsResponse>> get() = _articlesState

    private val _updateStatus = MutableLiveData<UpdateResult>()
    val updateStatus: LiveData<UpdateResult> get() = _updateStatus



    fun fetchSubmittedArticles(context: Context) {
        val url = "${Constants.BASE_URL}/api/articles/all"

        val request = object : JsonArrayRequest(
            Method.GET,
            url,
            null,
            { response ->
                try {
                    val articlesList = mutableListOf<NewsResponse>()
                    for (i in 0 until response.length()) {
                        val articleJson = response.getJSONObject(i)
                        // Only add articles with "submitted" status
                        if (articleJson.has("authorId") && !articleJson.isNull("authorId")) {
                            val authorJson = articleJson.getJSONObject("authorId")
                            val author = Author(
                                _id = authorJson.getString("_id"),
                                name = authorJson.optString("name", "Unknown"),  // Default value
                                email = authorJson.optString("email", "Unknown")
                            )
                            val article = NewsResponse(
                                _id = articleJson.getString("_id"),
                                title = articleJson.getString("title"),
                                content = articleJson.getString("content"),
                                authorId = author,
                                status = articleJson.getString("status"),
                                images = emptyList(),
                                createdAt = articleJson.getString("createdAt"),
                                __v = articleJson.getInt("__v")
                            )
                            articlesList.add(article)
                        } else {
                            Log.e("EditorHome", "Skipping article without authorId: ${articleJson.getString("_id")}")
                        }
                    }
                    _articlesState.value = articlesList
                } catch (e: JSONException) {
                    Log.e("EditorHome", "Error parsing articles: ${e.message}")
                }
            },
            { error ->
                Log.e("EditorHome", "Error fetching articles: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                // Add token if needed
                return headers
            }
        }

        Volley.newRequestQueue(context).add(request)
    }

    fun updateArticleStatus(context: Context, articleId: String, newStatus: String) {
        val url = "${Constants.BASE_URL}/api/articles/approve/$articleId"

        val requestBody = JSONObject().apply {
            put("status", newStatus)
        }

        val request = object : JsonObjectRequest(
            Method.PUT,
            url,
            requestBody,
            { response ->
                _updateStatus.value = UpdateResult(
                    isSuccess = true,
                    message = "Article $newStatus successfully"
                )
                // Refresh the articles list
                fetchSubmittedArticles(context)
            },
            { error ->
                _updateStatus.value = UpdateResult(
                    isSuccess = false,
                    message = "Failed to update article status: ${error.message}"
                )
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                // Add token if needed
                return headers
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}