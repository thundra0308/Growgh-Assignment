package com.example.assignmentgrowigh.activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.assignmentgrowigh.adapters.VideoAdapter
import com.example.assignmentgrowigh.databinding.ActivityVideoBinding
import com.example.assignmentgrowigh.models.ExoPlayerItem
import com.example.assignmentgrowigh.models.VideoModel
import com.example.assignmentgrowigh.utils.Constants

class VideoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoBinding
    private lateinit var adapter: VideoAdapter
    private var exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        val videoList: ArrayList<VideoModel> = arrayListOf()
        videoList.add(VideoModel("","","https://assets.mixkit.co/videos/preview/mixkit-small-pink-flowers-1186-large.mp4"))
        videoList.add(VideoModel("","","https://assets.mixkit.co/videos/preview/mixkit-blue-and-yellow-swirling-paint-1190-large.mp4"))
        videoList.add(VideoModel("","","https://assets.mixkit.co/videos/preview/mixkit-pink-and-blue-ink-1192-large.mp4"))
        videoList.add(VideoModel("","","https://assets.mixkit.co/videos/preview/mixkit-little-girl-and-her-father-playing-with-toys-at-christmas-39754-large.mp4"))
        if(Constants.isNetworkAvailable(this)) {
            setUpViewPager(videoList)
        } else {
            Toast.makeText(this@VideoActivity,"Please Enable the Internet Service to Access this Feature", Toast.LENGTH_SHORT).show()
        }
        binding.videoBtnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpViewPager(videoList: ArrayList<VideoModel>) {
        adapter = VideoAdapter(this,videoList,object : VideoAdapter.OnVideoPreparedListener{
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }

        })
        binding.videoViewpager.adapter = adapter
        binding.videoViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer.isPlaying }
                if(previousIndex!=-1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position==position }
                if(newIndex!=-1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.videoViewpager.currentItem }
        if(index!=-1) {
            val player = exoPlayerItems[index].exoPlayer
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val index = exoPlayerItems.indexOfFirst { it.position == binding.videoViewpager.currentItem }
        if(index!=-1) {
            val player = exoPlayerItems[index].exoPlayer
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(exoPlayerItems.isNotEmpty()) {
            for(item in exoPlayerItems) {
                val player = item.exoPlayer
                player.stop()
                player.clearMediaItems()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}