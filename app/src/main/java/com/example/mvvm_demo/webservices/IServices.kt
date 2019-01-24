package com.example.mvvm_demo.webservices

import androidx.lifecycle.LiveData
import com.example.mvvm_demo.database.entity.QuestionEntity
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface IServices{
    @GET("/users?since=135")
     fun loadQuestion(): Call<List<QuestionEntity>>
    @GET("/users?since=135")
     fun loadQuestion3(): Observable<List<QuestionEntity>>
    @GET("/users?since=135")
     fun loadQuestion2(): LiveData<List<QuestionEntity>>

}