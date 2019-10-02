package com.android.test.interfaces

import com.android.test.base.BaseRecyclerListener

interface OnRecyclerItemClickListener<T>: BaseRecyclerListener {
    fun onItemClickListener(item:T, flag:Boolean)
}