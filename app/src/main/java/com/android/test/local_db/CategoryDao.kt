package com.android.test.local_db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {

    @Query("SELECT * FROM Category")
    fun getAllCategory(): LiveData<List<Category>>

    @Insert
    fun insert(vararg category: Category)

    @Delete
    fun delete(category: Category)

    @Insert
    fun insertList(categoryList : List<Category>)

    @Update
    fun updateTodo(vararg category: Category)
}