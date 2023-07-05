package com.example.assignmentgrowigh.models

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("articles")
    val popularMovies: List<ResultModel>?
)