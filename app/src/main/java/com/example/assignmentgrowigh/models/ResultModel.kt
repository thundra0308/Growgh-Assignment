package com.example.assignmentgrowigh.models

import com.google.gson.annotations.SerializedName

data class ResultModel(
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("urlToImage")
    val urlToImage: String?,
    var likeCount: Long = 0,
    var isLiked: Boolean = false
)