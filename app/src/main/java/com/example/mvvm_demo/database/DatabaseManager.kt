package com.example.mvvm_demo.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mvvm_demo.WellBeApplication
import com.example.mvvm_demo.database.dao.QuestionDao
import com.example.mvvm_demo.database.entity.QuestionEntity
object DatabaseManager {
    private const val APP_DATABASE_NAME = "demo_test"

    val appDatabase by lazy {
        Room.databaseBuilder(WellBeApplication.context, QuestionDatabase::class.java, APP_DATABASE_NAME).build()
    }

    @Database(
        entities = [
            QuestionEntity::class
        ],
        version = 1
    )
    @TypeConverters()
    abstract class QuestionDatabase : RoomDatabase() {
        abstract fun questionDao(): QuestionDao
    }

}