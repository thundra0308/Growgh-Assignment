package com.example.assignmentgrowigh.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.models.ExoPlayerItem
import com.example.assignmentgrowigh.models.VideoModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource

class VideoAdapter(private val context: Context, private val videoList: ArrayList<VideoModel>, var videoPreparedListener: OnVideoPreparedListener): RecyclerView.Adapter<VideoAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemView: View, var videoPreparedListener: OnVideoPreparedListener): RecyclerView.ViewHolder(itemView) {
        fun bindData(video: VideoModel) {
//            val likeBtn = itemView.findViewById<CardView>(R.id.item_video_cv_like)
            val likeIcon = itemView.findViewById<ImageView>(R.id.item_video_iv_like)
            val zoomInAnim = AnimationUtils.loadAnimation(context,R.anim.zoom_in)
            val likeCount = itemView.findViewById<TextView>(R.id.item_video_tv_likecount)
            likeCount.text = video.likeCount.toString()
            likeIcon.setOnClickListener {
                if(video.isLiked) {
                    video.likeCount = video.likeCount - 1
                    likeCount.text = video.likeCount.toString()
                    video.isLiked = false
                    likeIcon.setImageResource(R.drawable.ic_like0)
                } else {
                    video.likeCount = video.likeCount + 1
                    likeCount.text = video.likeCount.toString()
                    video.isLiked = true
                    likeIcon.setImageResource(R.drawable.ic_likered)
                }
                likeIcon.startAnimation(zoomInAnim)
            }
            val exoPlayer = ExoPlayer.Builder(context).build()
            val pb = itemView.findViewById<ProgressBar>(R.id.item_video_progressbar)
            val player = itemView.findViewById<StyledPlayerView>(R.id.item_video_videoview)
            exoPlayer.addListener(object : Player.Listener{
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context,"Can't Play this Video",Toast.LENGTH_SHORT).show()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    if(playbackState==Player.STATE_BUFFERING) {
                        pb.visibility = View.VISIBLE
                    } else if(playbackState==Player.STATE_READY) {
                        pb.visibility = View.GONE
                    }
                }
            })
            player.player = exoPlayer
            exoPlayer.seekTo(0)
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(MediaItem.fromUri(Uri.parse(video.url)))
            exoPlayer.setMediaSource(mediaSource)
            exoPlayer.prepare()
            if(absoluteAdapterPosition==0) {
                exoPlayer.playWhenReady = true
                exoPlayer.play()
            }
            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer,absoluteAdapterPosition))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.items_video,parent,false)
        return MainViewHolder(view,videoPreparedListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(videoList[position])
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

}