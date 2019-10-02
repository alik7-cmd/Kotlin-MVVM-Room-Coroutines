package com.android.test.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

abstract class BaseFragment : DialogFragment(){

    private var mActivity: AppCompatActivity? = null

    @LayoutRes
    abstract fun layoutRes() : Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutRes(), container, false)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    fun getBaseActivity(): AppCompatActivity? {
        return mActivity
    }


}
