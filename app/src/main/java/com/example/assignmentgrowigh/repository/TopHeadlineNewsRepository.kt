package com.example.assignmentgrowigh.repository

import com.example.assignmentgrowigh.network.ApiService
import com.example.assignmentgrowigh.network.TopHeadlineNewsInterface

class TopHeadlineNewsRepository() {

    private val topHeadlineNewsApiService: TopHeadlineNewsInterface = ApiService.getInstance().create(TopHeadlineNewsInterface::class.java)
    fun getNews() = topHeadlineNewsApiService.getNewsList()

}
