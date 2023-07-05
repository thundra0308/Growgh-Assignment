package com.example.assignmentgrowigh.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assignmentgrowigh.adapters.NewsFeedAdapter
import com.example.assignmentgrowigh.databinding.ActivityFeedsBinding
import com.example.assignmentgrowigh.models.ResultModel
import com.example.assignmentgrowigh.utils.Constants
import com.example.assignmentgrowigh.utils.ScreenState
import com.example.assignmentgrowigh.viewmodels.FeedsViewModel


class FeedsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedsBinding
    private lateinit var viewModel: FeedsViewModel
    private var adapter: NewsFeedAdapter? = null
    private lateinit var newsList: List<ResultModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[FeedsViewModel::class.java]
        viewModel.topHeadlineLiveData.observe(this, Observer { state ->
            fetchTopHeadlines(state)
        })
        binding.feedRefresh.setOnRefreshListener {
            if(Constants.isNetworkAvailable(this@FeedsActivity)) {
                if(binding.feedRvNews.visibility==View.GONE && binding.feedProgressbar.visibility==View.GONE) {
                    finish()
                    startActivity(intent)
                }
                viewModel.topHeadlineLiveData.observe(this, Observer { state ->
                    fetchTopHeadlines(state)
                })
                Toast.makeText(this@FeedsActivity,"Refreshed",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@FeedsActivity,"Error Occurred",Toast.LENGTH_SHORT).show()
            }
            if(adapter!=null) {
                adapter?.notifyDataSetChanged()
            }
            binding.feedRefresh.isRefreshing = false
        }
    }

    private fun fetchTopHeadlines(state: ScreenState<List<ResultModel>?>) {
        when(state) {

            is ScreenState.Loading -> {
                binding.feedRvNews.visibility = View.GONE
                binding.feedProgressbar.visibility = View.VISIBLE
            }

            is ScreenState.Success -> {
                binding.feedRvNews.visibility = View.VISIBLE
                binding.feedProgressbar.visibility = View.GONE
                Log.e("DataResult","${state.data?.size}")
                newsList = state.data!!
                newsList = newsList.shuffled()
                adapter = NewsFeedAdapter(this@FeedsActivity,newsList)
                val rv = binding?.feedRvNews
                rv?.layoutManager = LinearLayoutManager(this@FeedsActivity, LinearLayoutManager.VERTICAL, false)
                rv?.adapter = adapter

            }

            else -> {
                binding.feedRvNews.visibility = View.GONE
                binding.feedProgressbar.visibility = View.GONE
                Toast.makeText(this@FeedsActivity,"Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

}