package com.example.assignmentgrowigh.models

data class VideoModel(
    var title: String?="",
    var desc: String?="",
    var url: String?="",
    var likeCount: Long=0,
    var isLiked: Boolean=false
)