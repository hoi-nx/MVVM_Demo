package com.example.mvvm_demo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvm_demo.database.dao.QuestionDao
import com.example.mvvm_demo.database.entity.QuestionEntity

@Database(
    entities = [
        QuestionEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase:RoomDatabase(){
        abstract fun questiondDao(): QuestionDao
        companion object {
            @Volatile
            private var INSTANCE: AppDatabase? = null

            fun getDatabase(context: Context): AppDatabase {
                val tempInstance = INSTANCE
                if (tempInstance != null) {
                    return tempInstance
                }
                synchronized(this) {
                    val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java,
                        "Data_Test"
                    ).build()
                    INSTANCE = instance
                    return instance
                }
            }
        }

}