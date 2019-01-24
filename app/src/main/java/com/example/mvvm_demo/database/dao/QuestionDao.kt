package com.example.mvvm_demo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mvvm_demo.database.entity.QuestionEntity

@Dao
interface QuestionDao{
    @Query("SELECT * from Question")
    fun getAllWords(): LiveData<List<QuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticles(articleEntities: List<QuestionEntity>)
}