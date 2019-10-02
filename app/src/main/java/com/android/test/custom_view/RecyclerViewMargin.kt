package com.android.test.custom_view

import androidx.recyclerview.widget.RecyclerView
import android.R.attr.left
import android.R.attr.right
import android.graphics.Rect
import android.view.View


class RecyclerViewMargin : RecyclerView.ItemDecoration() {

    private val columns: Int = 2
    private val margin: Int = 5

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        val position = parent.getChildLayoutPosition(view)
        //set right margin to all
        outRect.right = margin
        //set bottom margin to all
        outRect.bottom = margin
        //we only add top margin to the first row
        if (position < columns) {
            outRect.top = margin
        }
        //add left margin only to the first column
        if (position % columns === 0) {
            outRect.left = margin
        }
    }
}