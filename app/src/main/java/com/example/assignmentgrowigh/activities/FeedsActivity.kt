package com.example.assignmentgrowigh.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentgrowigh.adapters.NewsFeedAdapter
import com.example.assignmentgrowigh.databinding.ActivityFeedsBinding
import com.example.assignmentgrowigh.models.ResultModel
import com.example.assignmentgrowigh.utils.Constants
import com.example.assignmentgrowigh.utils.ScreenState
import com.example.assignmentgrowigh.utils.SwipeItemTouchHelper
import com.example.assignmentgrowigh.viewmodels.FeedsViewModel
import me.ibrahimsn.lib.NiceBottomBar


class FeedsActivity : AppCompatActivity(), SwipeItemTouchHelper.OnSwipeListener {

    private lateinit var binding: ActivityFeedsBinding
    private lateinit var viewModel: FeedsViewModel
    private var adapter: NewsFeedAdapter? = null
    private lateinit var newsList: List<ResultModel>
    private lateinit var rv: RecyclerView
    private var scrollState: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding.mainNavBar.setActiveItem(0)
        val bottomBar: NiceBottomBar = binding.mainNavBar
        bottomBar.onItemSelected = {
            when(it) {
                1 -> {
                    startActivity(Intent(this@FeedsActivity,VideoActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this@FeedsActivity,MapsActivity::class.java))
                }
            }
        }
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
        binding.feedactivityAddFab.setOnClickListener {
            startActivity(Intent(this@FeedsActivity,UploadImageActivity::class.java))
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
                rv = binding.feedRvNews
                rv?.layoutManager = LinearLayoutManager(this@FeedsActivity, LinearLayoutManager.VERTICAL, false)
                rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if(dy>0 && (scrollState==0 || scrollState==2)) {
                            //TODO HIDE
                        } else if(dy<-10){
                            //TODO SHOW
                        }
                        if (dy > 0 && binding.feedactivityAddFab.visibility == View.VISIBLE) {
                            binding.feedactivityAddFab.hide()
                        } else if (dy < 0 && binding.feedactivityAddFab.visibility !== View.VISIBLE) {
                            binding.feedactivityAddFab.show()
                        }
                    }
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        scrollState = newState
                    }
                })
                rv?.adapter = adapter
                val swipeItemTouchHelper = SwipeItemTouchHelper(this)

                // Attach the SwipeItemTouchHelper to the RecyclerView
                val itemTouchHelper = ItemTouchHelper(swipeItemTouchHelper)
                itemTouchHelper.attachToRecyclerView(rv)
            }

            else -> {
                binding.feedRvNews.visibility = View.GONE
                binding.feedProgressbar.visibility = View.GONE
                Toast.makeText(this@FeedsActivity,"Error Occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun shareContent(position: Int) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = newsList[position].urlToImage
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onResume() {
        super.onResume()
        binding.mainNavBar.setActiveItem(0)
    }

    override fun onItemSwiped(position: Int) {
        shareContent(position)
        adapter?.notifyItemChanged(position)
    }

}