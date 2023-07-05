package com.example.assignmentgrowigh.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.adapters.OnboardingItemsAdapter
import com.example.assignmentgrowigh.animations.StackTransformer
import com.example.assignmentgrowigh.databinding.ActivityOnboardingBinding
import com.example.assignmentgrowigh.models.OnboardingItems
import com.example.assignmentgrowigh.utils.Constants

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = this.getSharedPreferences(Constants.MY_PREFERENCES,Context.MODE_PRIVATE)
        if(sharedPref.getBoolean(Constants.IS_INTRO_COMPLETE,false)) {
            startActivity(Intent(this@OnboardingActivity,WelcomeActivity::class.java))
            finish()
        }
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val viewPager: ViewPager2 = binding.introViewpager2
        val itemsList: ArrayList<OnboardingItems> = arrayListOf()
        val item1 = OnboardingItems(R.drawable.ic_img1,"About Us","hdsfhdshjfhsdfhsdhfdshfushdfhsldhflsdhflsdhfsdlfhdslfhslsdfhlsHuhsfhulsdhflfhsdlsahdlfhalsdfhlhsadhfashdfudlsahfdulsaudsdslfslhdf")
        val item2 = OnboardingItems(R.drawable.ic_img2,"Our Mission","hdsfhdshjfhsdfhsdhfdshfushdfhsldhflsdhflsdhfsdlfhdslfhslsdfhlsHuhsfhulsdhflfhsdlsahdlfhalsdfhlhsadhfashdfudlsahfdulsaudsdslfslhdf")
        val item3 = OnboardingItems(R.drawable.ic_img3,"Our Vision","hdsfhdshjfhsdfhsdhfdshfushdfhsldhflsdhflsdhfsdlfhdslfhslsdfhlsHuhsfhulsdhflfhsdlsahdlfhalsdfhlhsadhfashdfudlsahfdulsaudsdslfslhdf")
        itemsList.add(item1)
        itemsList.add(item2)
        itemsList.add(item3)
        val adapter = OnboardingItemsAdapter(this,itemsList,viewPager,this@OnboardingActivity)
        binding.introViewpager2.adapter = adapter
        binding.introViewpager2.apply {
            setPageTransformer(StackTransformer())
        }
    }

}