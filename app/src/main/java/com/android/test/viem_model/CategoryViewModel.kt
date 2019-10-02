package com.android.test.viem_model

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.android.test.local_db.entity.Category
import com.android.test.repository.Repository
import java.util.*
import kotlin.collections.ArrayList

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    var categoryLiveData: LiveData<List<Category>>? = null

    var mRepository: Repository? = null

    init {

        mRepository = Repository(application)

        categoryLiveData = mRepository?.getAllCategory()

    }

    internal fun getAllCategoryList(): LiveData<List<Category>>? {
        return categoryLiveData
    }

    internal fun saveSampleData() {
        mRepository?.insertCategoryList(getSampleList())
    }

    private fun getSampleList(): List<Category> {

        val listOfCategory = ArrayList<Category>()
        listOfCategory.add(
            Category(
                UUID.randomUUID().toString(),
                "Test 0",
                "Test 0"
            )
        )
        listOfCategory.add(
            Category(
                UUID.randomUUID().toString(),
                "Test 1",
                "Test 1"
            )
        )
        listOfCategory.add(
            Category(
                UUID.randomUUID().toString(),
                "Test 2",
                "Test 2"
            )
        )

        return listOfCategory
    }

    fun insertCategory(category: Category){
        mRepository?.insertCategory(category)
    }

    fun checkValidity(name: String, description : String) : Boolean{

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(description)){
            return true
        }

        return false

    }

}