package com.android.test.adapter
import android.view.View
import com.android.test.interfaces.BaseRecyclerListener
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder <T,L : BaseRecyclerListener>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind(item: T, listener: L?)
}