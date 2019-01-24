package com.example.mvvm_demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_demo.database.entity.QuestionEntity

class QuestionModel:ViewModel(){
    private val loginRepository by lazy {
        QuestionRepository(AppExecutors())
    }

    fun loadQuestion(): LiveData<Resource<List<QuestionEntity>>>{
        return loginRepository.loadQuestion()
    }



}