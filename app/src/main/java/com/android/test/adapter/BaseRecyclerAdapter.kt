package com.android.test.adapter
import com.android.test.interfaces.BaseRecyclerListener
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import java.util.ArrayList

abstract class BaseRecyclerAdapter<T, L : BaseRecyclerListener, VH : BaseViewHolder<T, L>>(val context: Context): RecyclerView.Adapter<VH>()
{

    internal var items: MutableList<T>? = null
    private var listener: L? = null
    private lateinit var layoutInflater: LayoutInflater
    private var position: Int = 0

    init {
        layoutInflater = LayoutInflater.from(context)
        items = ArrayList<T>()
    }

    abstract override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): VH

    override fun onBindViewHolder(p0: VH, @SuppressLint("RecyclerView") p1: Int) {
        val item = items!!.get(p1)
        p0.onBind(item, listener)
        position = p1
    }

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    fun setItems(items: List<T>?) {
        if (items == null) {
            throw IllegalArgumentException("Cannot set `null` item to the Recycler adapter")
        }
        this.items!!.clear()
        this.items!!.addAll(items)
        notifyDataSetChanged()
    }

    fun getItems(): MutableList<T>? {
        return this.items
    }

    fun add(item: T?) {
        if (item == null) {
            throw IllegalArgumentException("Cannot add null item to the Recycler adapter")
        }
        items!!.add(item)
        notifyItemInserted(items!!.size - 1)
    }

    fun addAll(items: List<T>?) {
        if (items == null) {
            throw IllegalArgumentException("Cannot add `null` items to the Recycler adapter")
        }
        this.items!!.addAll(items)
        notifyItemRangeInserted(this.items!!.size - items.size, items.size)
    }

    fun clear() {
        items!!.clear()
        notifyDataSetChanged()
    }

    fun remove(item: T) {
        val position = items!!.indexOf(item)
        if (position > -1) {
            items!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun setListener(listener: L) {
        this.listener = listener
    }

    protected fun inflate(@LayoutRes layout: Int, parent: ViewGroup?, attachToRoot: Boolean): View {
        return layoutInflater.inflate(layout, parent, attachToRoot)
    }

    protected fun inflate(@LayoutRes layout: Int, parent: ViewGroup?): View {
        return inflate(layout, parent, false)
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(hasStableIds)
    }
}