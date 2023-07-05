package com.example.assignmentgrowigh.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.activities.OnboardingActivity
import com.example.assignmentgrowigh.activities.WelcomeActivity
import com.example.assignmentgrowigh.models.OnboardingItems
import com.mikhaellopez.circularprogressbar.CircularProgressBar

class OnboardingItemsAdapter(private val context: Context, private val items: ArrayList<OnboardingItems>,private val viewPager: ViewPager2, private val activity: OnboardingActivity): RecyclerView.Adapter<OnboardingItemsAdapter.MainViewHolder>() {
    inner class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindData(item: OnboardingItems) {
            val skipBtn = itemView.findViewById<CardView>(R.id.intro_btn_skip)
            val nextBtn = itemView.findViewById<CardView>(R.id.intro_next_btn)
            val imageView = itemView.findViewById<ImageView>(R.id.intro_img)
            val headerText = itemView.findViewById<TextView>(R.id.intro_tv_head)
            val descText = itemView.findViewById<TextView>(R.id.intro_tv_desc)
            val progressBar = itemView.findViewById<CircularProgressBar>(R.id.intro_progress_bar)
            val btnReady = itemView.findViewById<CardView>(R.id.intro_btn_ready)
            skipBtn.setOnClickListener {
                val intent = Intent(context,WelcomeActivity::class.java)
                startActivity(context,intent,null)
            }
            if(adapterPosition==items.size-1) {
                progressBar.visibility = View.GONE
                nextBtn.visibility = View.GONE
                btnReady.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.VISIBLE
                nextBtn.visibility = View.VISIBLE
                btnReady.visibility = View.GONE
            }
            nextBtn.setOnClickListener {
                viewPager.setCurrentItem(adapterPosition+1,true)
                notifyDataSetChanged()
            }
            btnReady.setOnClickListener {
                if(btnReady.visibility==View.VISIBLE) {
                    val intent = Intent(context, WelcomeActivity::class.java)
                    startActivity(context, intent, null)
                    activity.finish()
                }
            }
            imageView.setImageResource(item.img)
            headerText.text = item.header
            descText.text = item.desc
            progressBar.progress = (adapterPosition+1).toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_container_introviewpager,parent,false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}