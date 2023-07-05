package com.example.assignmentgrowigh.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignmentgrowigh.databinding.ActivityWelcomeBinding
import com.example.assignmentgrowigh.utils.Constants

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = this.getSharedPreferences(Constants.MY_PREFERENCES,Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Constants.IS_INTRO_COMPLETE, true)
        editor.apply()
        setupListeners()
    }

    private fun setupListeners() {
        binding.welcomeBtnUploadimage.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity,UploadImageActivity::class.java))
        }
        binding.welcomeBtnFeeds.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity,FeedsActivity::class.java))
        }
    }

}