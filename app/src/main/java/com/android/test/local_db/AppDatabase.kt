package com.android.test.local_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.*
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Category::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Sample.db").allowMainThreadQueries()
                .build()
    }
}