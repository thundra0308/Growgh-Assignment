package com.example.assignmentgrowigh.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.assignmentgrowigh.models.ResultModel
import com.example.assignmentgrowigh.models.NewsResponse
import com.example.assignmentgrowigh.repository.TopHeadlineNewsRepository
import com.example.assignmentgrowigh.utils.ScreenState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedsViewModel(private val repository: TopHeadlineNewsRepository = TopHeadlineNewsRepository()): ViewModel() {
    private var topHeadlinesLiveData: MutableLiveData<ScreenState<List<ResultModel>?>> = MutableLiveData()
    val topHeadlineLiveData: LiveData<ScreenState<List<ResultModel>?>>
        get() = topHeadlinesLiveData

    init {
        fetchTopHeadlinesList()
    }

    private fun fetchTopHeadlinesList() {
        val client = repository.getNews()
        topHeadlinesLiveData.postValue(ScreenState.Loading(null))
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if(response.isSuccessful) {
                    topHeadlinesLiveData.postValue(ScreenState.Success(response.body()?.popularMovies))
                } else {
                    topHeadlinesLiveData.postValue(ScreenState.Error(null,response.code().toString()))
                }
            }
            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                Log.e("Failed Pathetically",""+t.message)
                topHeadlinesLiveData.postValue(ScreenState.Error(null,t.message.toString()))
            }
        })
    }
}