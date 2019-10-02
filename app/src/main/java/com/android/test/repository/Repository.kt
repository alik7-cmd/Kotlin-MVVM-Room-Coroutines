package com.android.test.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.android.test.local_db.AppDatabase
import com.android.test.local_db.Category
import com.android.test.local_db.CategoryDao
import android.os.AsyncTask



class Repository(private var application: Application) {
    var mDao: CategoryDao

    var allCategoryList: LiveData<List<Category>>? = null

    init {
        mDao = AppDatabase.getInstance(application).categoryDao()
        allCategoryList = mDao.getAllCategory()

    }

    fun getAllCategory(): LiveData<List<Category>>? {
        return allCategoryList
    }

    fun insertCategory(category: Category){
        InsertCategoryAsyncTask(mDao).execute(category)
    }

    fun insertCategoryList(categoryList: List<Category>){
        InsertCategoryListAsyncTask(mDao).execute(categoryList)
    }

    private class InsertCategoryAsyncTask internal constructor(private val categoryDao: CategoryDao) :
        AsyncTask<Category, Void, Void>() {

        override fun doInBackground(vararg category: Category): Void? {
            categoryDao.insert(category[0])
            return null
        }
    }

    private class InsertCategoryListAsyncTask internal constructor(private val categoryDao: CategoryDao) :
        AsyncTask<List<Category>, Void, Void>() {

        override fun doInBackground(vararg categoryList: List<Category>): Void? {
            categoryDao.insertList(categoryList[0])
            return null
        }
    }

}