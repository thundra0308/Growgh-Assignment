package com.example.assignmentgrowigh.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.assignmentgrowigh.R
import com.example.assignmentgrowigh.adapters.NewsFeedAdapter

class SwipeItemTouchHelper(private val onSwipeListener: OnSwipeListener) :
    ItemTouchHelper.Callback() {

    private val swipeThreshold = 0.1f
    private val iconMargin = 20

    interface OnSwipeListener {
        fun onItemSwiped(position: Int)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onSwipeListener.onItemSwiped(position)
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return swipeThreshold
    }

    override fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val paint = Paint()
        val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_share0)
        val iconHeight = icon?.intrinsicHeight ?: 0
        val iconWidth = icon?.intrinsicWidth ?: 0

        if (dX > 0) {
            val alpha = 0 / recyclerView.width.toFloat()
            itemView.alpha = 1.toFloat() // Apply transparency to the swiped item

            val maxDx = recyclerView.width * swipeThreshold

            val finalDx = if (dX <= maxDx) dX else maxDx

            viewHolder.itemView.translationX = finalDx

            paint.color = Color.parseColor("#3478A9")
            val background = RectF(
                itemView.left.toFloat(),
                itemView.top.toFloat(),
                itemView.left + finalDx,
                itemView.bottom.toFloat()
            )
            c.drawRoundRect(background,10f,10f, paint)

            if (icon != null) {
                val iconTop = itemView.top + (itemView.height - iconHeight) / 2
                val iconMargin = 30
                val iconLeft = itemView.left + iconMargin
                val iconRight = iconLeft + iconWidth
                val iconBottom = iconTop + iconHeight

                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                icon.draw(c)
            }
            var newDx = dX;
            if (newDx >= 100f) {
                newDx = 100f
            }
            super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive)
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        viewHolder.itemView.alpha = 1.0f
        viewHolder.itemView.translationX = 0f
    }
}