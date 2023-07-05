package com.example.assignmentgrowigh.animations

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class StackTransformer : ViewPager2.PageTransformer {

    private val scaleFactor = 0.85f // Adjust the scale factor as needed

    override fun transformPage(page: View, position: Float) {
        when {
            position < -1 -> { // Page is off-screen to the left
                page.alpha = 0f
            }
            position <= 0 -> { // Page is in the center of the screen
                page.alpha = 1f
                page.translationX = 0f
                page.scaleX = 1f
                page.scaleY = 1f
            }
            position <= 1 -> { // Page is off-screen to the right
                page.alpha = 1 - position
                page.translationX = -page.width * position
                val scaleFactor = scaleFactor + (1 - scaleFactor) * (1 - Math.abs(position))
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
            }
            else -> { // Page is off-screen to the right
                page.alpha = 0f
            }
        }
        val animDuration = if (position == 0f) 1000L else 0L
        page.animate()
            .setDuration(animDuration)
            .start()
    }

}