package com.example.assignmentgrowigh.network

import com.example.assignmentgrowigh.models.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface TopHeadlineNewsInterface {
    @GET("/v2/top-headlines?country=in&language=en&category=technology&apiKey=6c3957a92cc64201bd80a6db1968bfa4")
    fun getNewsList(): Call<NewsResponse>
}