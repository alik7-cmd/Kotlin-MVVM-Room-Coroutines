package com.android.test.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.test.R
import com.android.test.adapter.CategoryAdapter
import com.android.test.base.BaseActivity
import com.android.test.local_db.Category
import com.android.test.viem_model.CategoryViewModel

import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import com.android.test.custom_view.RecyclerViewMargin
import com.android.test.fragment.CategoryAddFragment
import com.android.test.interfaces.OnRecyclerItemClickListener


class HomeActivity : BaseActivity(), OnRecyclerItemClickListener<Category> {

    override fun layoutRes(): Int {
        return R.layout.activity_home
    }

    override fun onItemClickListener(item: Category, flag: Boolean) {
    }

    var mAdapter: CategoryAdapter? = null
    var mViewModel: CategoryViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        setSupportActionBar(toolbar)
        initView()
        observeLiveData()
    }

    fun initView() {
        rv_category.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_category.itemAnimator = DefaultItemAnimator()
        val decoration = RecyclerViewMargin()
        rv_category.addItemDecoration(decoration)
    }


    fun observeLiveData() {

        mViewModel?.getAllCategoryList()?.observe(this, Observer { data ->
            initAdapter(data)
        })
    }

    fun initAdapter(data: List<Category>) {
        mAdapter = CategoryAdapter(applicationContext)
        mAdapter!!.setItems(data)
        mAdapter!!.setListener(this)
        rv_category.adapter = mAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_category -> {
                val inputFragment = CategoryAddFragment.newInstance()
                inputFragment.show(supportFragmentManager, "CategoryAddFragment")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

