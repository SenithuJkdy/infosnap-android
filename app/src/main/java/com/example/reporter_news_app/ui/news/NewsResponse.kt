package com.example.reporter_news_app.ui.news

data class NewsResponse(
    val _id: String,
    val title: String,
    val content: String,
    val authorId: Author,
    val status: String,
    val images: List<String>,
    val createdAt: String,
    val __v: Int
)


