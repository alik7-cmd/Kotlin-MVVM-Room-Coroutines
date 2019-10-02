package com.android.test.interfaces

interface OnRecyclerItemClickListener<T>:BaseRecyclerListener {
    fun onItemClickListener(item:T, flag:Boolean)
}