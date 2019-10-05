package com.android.test.repository

import android.app.Application
import com.android.test.local_db.database.AppDatabase
import com.android.test.local_db.entity.Category
import com.android.test.local_db.dao.CategoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext


class Repository(private var application: Application) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    var mDao: CategoryDao

    init {
        mDao = AppDatabase.getInstance(application).categoryDao()
    }

    fun getAllCategory() = mDao.getAllCategory()

    fun insertCategory(category: Category) {
        launch { insertCategorySuspend(category) }
    }

    fun insertCategoryList(categoryList: List<Category>) {
        launch { insertCategoryListSuspend(categoryList) }
    }

    private suspend fun insertCategorySuspend(category: Category) {
        withContext(Dispatchers.IO) {
            mDao.insert(category)
        }
    }

    private suspend fun insertCategoryListSuspend(categoryList: List<Category>) {
        withContext(Dispatchers.IO) {
            mDao.insertList(categoryList)
        }
    }

}