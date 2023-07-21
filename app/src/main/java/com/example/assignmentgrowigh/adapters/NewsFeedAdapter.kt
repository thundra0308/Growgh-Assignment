package com.example.assignmentgrowigh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.models.ResultModel

class NewsFeedAdapter(private val context: Context, private val newsList: List<ResultModel>): RecyclerView.Adapter<NewsFeedAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(news: ResultModel) {
            val image = itemView.findViewById<ImageView>(R.id.feedrv_iv_image)
            val title = itemView.findViewById<TextView>(R.id.feedrv_tv_title)
            val author = itemView.findViewById<TextView>(R.id.feedrv_tv_author)
            val zoomInAnim = AnimationUtils.loadAnimation(context,R.anim.zoom_in)
            val likeIcon = itemView.findViewById<ImageView>(R.id.feedrv_iv_likeicon)
            val likeCount = itemView.findViewById<TextView>(R.id.feedrv_tv_likecount)
            likeIcon.setOnClickListener {
                if(news.isLiked) {
                    news.likeCount = news.likeCount - 1
                    likeCount.text = news.likeCount.toString()
                    news.isLiked = false
                    likeIcon.setImageResource(R.drawable.ic_like1)
                } else {
                    news.likeCount = news.likeCount + 1
                    likeCount.text = news.likeCount.toString()
                    news.isLiked = true
                    likeIcon.setImageResource(R.drawable.ic_likered)
                }
                likeIcon.startAnimation(zoomInAnim)
            }
            Glide
                .with(context)
                .load(news.urlToImage)
                .centerCrop()
                .into(image)
            title.text = news.title
            author.text = news.author
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_rv_feed,parent,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}