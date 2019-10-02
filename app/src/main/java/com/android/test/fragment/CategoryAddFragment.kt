package com.android.test.fragment


import androidx.fragment.app.Fragment


import com.android.test.R
import com.android.test.base.BaseFragment



/**
 * A simple [Fragment] subclass.
 */
class CategoryAddFragment : BaseFragment() {
    override fun layoutRes(): Int {
        return R.layout.fragment_category_add
    }

    companion object{
        fun newInstance(): CategoryAddFragment {
            val frag = CategoryAddFragment()
            return frag
        }
    }






}
